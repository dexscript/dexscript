package com.dexscript.transpile.type.java;

import com.dexscript.ast.type.DexType;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.JavaTypes;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaClassType implements NamedType, FunctionsProvider, GenericType {

    private final OutShim oShim;
    private final Class clazz;
    private final List<Type> typeArgs;
    private Map<TypeVariable, Type> javaTypeVarMap;
    private List<FunctionType> functions;
    private List<Type> typeParams;
    private ArrayList<PlaceholderType> placeholders;
    private TypeSystem ts;
    private String description;

    public JavaClassType(OutShim oShim, Class clazz) {
        this(oShim, clazz, null);
    }

    public JavaClassType(OutShim oShim, Class clazz, List<Type> typeArgs) {
        this.oShim = oShim;
        this.clazz = clazz;
        this.typeArgs = typeArgs;
        this.ts = oShim.typeSystem();
        if (typeArgs == null) {
            oShim.javaTypes().add(clazz, this);
            ts.defineType(this);
        }
        ts.lazyDefineFunctions(this);
    }

    @Override
    public @NotNull String name() {
        return clazz.getSimpleName();
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        typeParameters(); // calculate placeholders
        functions = new ArrayList<>();
        newFuncs(functions);
        javaMethodToDexFuncs(functions);
        return functions;
    }

    private void javaMethodToDexFuncs(List<FunctionType> collector) {
        for (Method method : clazz.getMethods()) {
            javaMethodToDexFunc(collector, method);
        }
    }

    private void javaMethodToDexFunc(List<FunctionType> collector, Method method) {
        JavaTypes javaTypes = oShim.javaTypes();
        Type ret = resolveJavaType(method.getGenericReturnType());
        if (ret == null) {
            return;
        }
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        for (Class<?> paramClazz : method.getParameterTypes()) {
            Type param = javaTypes.tryResolve(paramClazz);
            if (param == null) {
                return;
            }
            params.add(param);
        }
        FunctionType function = new FunctionType(method.getName(), params, ret);
        function.attach((FunctionType.LazyAttachment) () -> new CallJavaMethod(oShim, function, method));
        collector.add(function);
    }

    private Type resolveJavaType(java.lang.reflect.Type javaType) {
        if (javaType instanceof Class) {
            return oShim.javaTypes().tryResolve((Class) javaType);
        }
        if (javaType instanceof TypeVariable) {
            TypeVariable typeVar = (TypeVariable) javaType;
            return javaTypeVarMap().get(typeVar);
        }
        return null;
    }

    private void newFuncs(List<FunctionType> collector) {
        String subClassName = null;
        for (Constructor ctor : clazz.getConstructors()) {
            if (subClassName == null) {
                if (typeArgs == null) {
                    subClassName = clazz.getCanonicalName();
                } else {
                    subClassName = oShim.genSubClass(clazz);
                    oShim.javaTypes().add(subClassName, this);
                }
            }
            newFunc(collector, ctor, subClassName);
        }
    }

    private void newFunc(List<FunctionType> collector, Constructor ctor, String subClassName) {
        ArrayList<Type> params = new ArrayList<>();
        String funcName = clazz.getSimpleName();
        params.add(new StringLiteralType(funcName));
        for (Class paramType : ctor.getParameterTypes()) {
            Type type = oShim.javaTypes().tryResolve(paramType);
            if (type == null) {
                return;
            }
            params.add(type);
        }
        FunctionSig sig = new FunctionSig(placeholders, params, this, createRetElem());
        FunctionType function = new FunctionType("New__", params, this, sig);
        function.attach((FunctionType.LazyAttachment) () -> new NewJavaClass(oShim, function, ctor, subClassName));
        collector.add(function);
    }

    private DexType createRetElem() {
        if (placeholders.isEmpty()) {
            return null;
        }
        StringBuilder expand = new StringBuilder();
        expand.append(name());
        expand.append('<');
        for (int i = 0; i < placeholders.size(); i++) {
            if (i > 0) {
                expand.append(", ");
            }
            String placeholder = placeholders.get(i).name();
            expand.append(placeholder);
        }
        expand.append('>');
        DexType retElem = DexType.parse(expand.toString());
        return retElem;
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new JavaClassType(oShim, clazz, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        if (typeParams != null) {
            return typeParams;
        }
        typeParams = new ArrayList<>();
        placeholders = new ArrayList<>();
        for (TypeVariable javaTypeVar : clazz.getTypeParameters()) {
            Type typeParam = translateBound(javaTypeVar.getBounds());
            typeParams.add(typeParam);
            placeholders.add(new PlaceholderType(javaTypeVar.getName(), typeParam));
        }
        return typeParams;
    }

    private Map<TypeVariable, Type> javaTypeVarMap() {
        if (javaTypeVarMap != null) {
            return javaTypeVarMap;
        }
        javaTypeVarMap = new HashMap<>();
        List<Type> typeArgs = this.typeArgs;
        if (typeArgs == null) {
            typeArgs = typeParameters();
        }
        TypeVariable[] javaTypeVars = clazz.getTypeParameters();
        for (int i = 0; i < typeArgs.size(); i++) {
            javaTypeVarMap.put(javaTypeVars[i], typeArgs.get(i));
        }
        return javaTypeVarMap;
    }

    private Type translateBound(java.lang.reflect.Type[] bounds) {
        // TODO: translate bound
        return BuiltinTypes.ANY;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        return ts.isSubType(ctx, this, that);
    }

    @Override
    public String description() {
        if (description == null) {
            description = describe(typeArgs);
        }
        return description;
    }
}
