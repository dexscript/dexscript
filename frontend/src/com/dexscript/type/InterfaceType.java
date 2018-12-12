package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.ast.inf.DexInfTypeParam;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InterfaceType extends NamedType implements GenericType, FunctionsProvider {

    private final TypeTable typeTable;
    private final FunctionTable functionTable;
    private final DexInterface inf;
    private List<Type> typeArgs;
    private List<FunctionType> members;
    private List<Type> typeParams;

    public InterfaceType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable, @NotNull DexInterface inf) {
        this(typeTable, functionTable, inf, null);
    }

    public InterfaceType(@NotNull TypeTable typeTable, @NotNull FunctionTable functionTable,
                         @NotNull DexInterface inf, List<Type> typeArgs) {
        super(inf.identifier().toString(), "Object");
        this.typeArgs = typeArgs;
        typeTable.define(this);
        functionTable.lazyDefine(this);
        this.typeTable = typeTable;
        this.functionTable = functionTable;
        this.inf = inf;
    }

    public List<FunctionType> functions() {
        if (members != null) {
            return members;
        }
        if (typeArgs == null) {
            typeArgs = typeParameters();
        }
        members = new ArrayList<>();
        TypeTable localTypeTable = new TypeTable(typeTable);
        for (int i = 0; i < inf.typeParams().size(); i++) {
            DexInfTypeParam typeParam = inf.typeParams().get(i);
            String typeParamName = typeParam.paramName().toString();
            localTypeTable.define(typeParamName, typeArgs.get(i));
        }
        for (DexInfMethod method : inf.methods()) {
            addInfMethod(localTypeTable, method);
        }
        for (DexInfFunction function : inf.functions()) {
            addInfFunction(localTypeTable, function);
        }
        return members;
    }

    private void addInfFunction(TypeTable localTypeTable, DexInfFunction infFunction) {
        String name = infFunction.identifier().toString();
        List<Type> params = new ArrayList<>();
        for (DexParam param : infFunction.sig().params()) {
            params.add(ResolveType.$(localTypeTable, param.paramType()));
        }
        Type ret = ResolveType.$(localTypeTable, infFunction.sig().ret());
        members.add(new FunctionType(name, params, ret));
    }

    private void addInfMethod(TypeTable localTypeTable, DexInfMethod infMethod) {
        String name = infMethod.identifier().toString();
        List<Type> params = new ArrayList<>();
        params.add(this);
        for (DexParam param : infMethod.sig().params()) {
            params.add(ResolveType.$(localTypeTable, param.paramType()));
        }
        Type ret = ResolveType.$(localTypeTable, infMethod.sig().ret());
        members.add(new FunctionType(name, params, ret));
    }

    @Override
    public boolean isAssignableFrom(Substituted substituted, Type that) {
        if (super.isAssignableFrom(substituted, that)) {
            return true;
        }
        return functionTable.isAssignableFrom(substituted, this, that);
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new InterfaceType(typeTable, functionTable, inf, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        if (typeParams == null) {
            typeParams = new ArrayList<>();
            for (DexInfTypeParam typeParam : inf.typeParams()) {
                typeParams.add(ResolveType.$(typeTable, typeParam.paramType()));
            }
        }
        return typeParams;
    }

    public List<Type> typeArgs() {
        return typeArgs;
    }
}
