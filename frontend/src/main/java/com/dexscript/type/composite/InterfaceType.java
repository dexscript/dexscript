package com.dexscript.type.composite;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.ast.inf.DexInfTypeParam;
import com.dexscript.type.core.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceType implements NamedType, GenericType, CompositeType {

    static {
        InferTypeTable.register(DexInterface.class, (ts, typeTableMap, elem) -> {
            if (typeTableMap == null) {
                typeTableMap = new HashMap<>();
            }
            TypeTable typeTable = new TypeTable(ts);
            typeTableMap.put(elem, typeTable);
            for (DexInfTypeParam typeParam : elem.typeParams()) {
                String name = typeParam.paramName().toString();
                DType type = InferType.$(ts, typeTableMap, typeParam.paramType());
                typeTable.define(elem.pkg(), name, type);
            }
            return typeTable;
        });
        InferTypeTable.register(DexSig.class, (ts, typeTableMap, elem) -> {
            if (typeTableMap == null) {
                typeTableMap = new HashMap<>();
            }
            TypeTable typeTable = new TypeTable(ts);
            typeTableMap.put(elem, typeTable);
            for (DexTypeParam typeParam : elem.typeParams()) {
                String name = typeParam.paramName().toString();
                DType type = InferType.$(ts, typeTableMap, typeParam.paramType());
                typeTable.define(elem.pkg(), name, type);
            }
            return typeTable;
        });
        InferTypeTable.register(DexInfFunction.class, (ts, typeTableMap, elem) -> {
            if (typeTableMap == null) {
                typeTableMap = new HashMap<>();
            }
            typeTableMap.put(elem, null);
            return InferTypeTable.$(ts, typeTableMap, elem.sig());
        });
        InferTypeTable.register(DexInfMethod.class, (ts, typeTableMap, elem) -> {
            if (typeTableMap == null) {
                typeTableMap = new HashMap<>();
            }
            typeTableMap.put(elem, null);
            return InferTypeTable.$(ts, typeTableMap, elem.sig());
        });
    }

    public static void init() {
    }

    private final TypeSystem ts;
    private final DexInterface inf;
    private List<DType> typeArgs;
    private List<FunctionType> functions;
    private List<DType> typeParams;
    private String description;

    public InterfaceType(@NotNull TypeSystem ts, @NotNull DexInterface inf) {
        this(ts, inf, null);
    }

    public InterfaceType(@NotNull TypeSystem ts, @NotNull DexInterface inf, List<DType> typeArgs) {
        this.ts = ts;
        this.typeArgs = typeArgs;
        this.inf = inf;
        if (typeArgs == null) {
            ts.defineType(this);
        }
        ts.lazyDefineFunctions(this);
    }

    @Override
    public @NotNull String name() {
        return inf.identifier().toString();
    }

    @Override
    public DexPackage pkg() {
        return inf.pkg();
    }

    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        List<DType> typeArgs = this.typeArgs;
        if (typeArgs == null) {
            typeArgs = typeParameters();
        }
        functions = new ArrayList<>();
        TypeTable localTypeTable = new TypeTable(ts);
        for (int i = 0; i < inf.typeParams().size(); i++) {
            DexInfTypeParam typeParam = inf.typeParams().get(i);
            String typeParamName = typeParam.paramName().toString();
            localTypeTable.define(inf.pkg(), typeParamName, typeArgs.get(i));
        }
        Map<DexElement, TypeTable> typeTableMap = new HashMap<>();
        typeTableMap.put(inf, localTypeTable);
        for (DexInfMethod method : inf.methods()) {
            addInfMethod(typeTableMap, method);
        }
        for (DexInfFunction function : inf.functions()) {
            addInfFunction(typeTableMap, function);
        }
        return functions;
    }

    private void addInfFunction(Map<DexElement, TypeTable> typeTableMap, DexInfFunction infFunction) {
        String name = infFunction.identifier().toString();
        FunctionType functionType = new FunctionType(ts, name, typeTableMap, infFunction.sig());
        functions.add(functionType);
    }

    private void addInfMethod(Map<DexElement, TypeTable> typeTableMap, DexInfMethod infMethod) {
        String name = infMethod.identifier().toString();
        FunctionType functionType = new FunctionType(ts, name, typeTableMap, infMethod.asFuncSig());
        functions.add(functionType);
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return ts.functionTable().isAssignable(ctx, this, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        if (description == null) {
            description = describe(typeArgs);
        }
        return description;
    }

    @Override
    public DType generateType(List<DType> typeArgs) {
        return new InterfaceType(ts, inf, typeArgs);
    }

    @Override
    public List<DType> typeParameters() {
        if (typeParams == null) {
            typeParams = new ArrayList<>();
            for (DexInfTypeParam typeParam : inf.typeParams()) {
                typeParams.add(InferType.$(ts, typeParam.paramType()));
            }
        }
        return typeParams;
    }

    public List<DType> typeArgs() {
        return typeArgs;
    }
}
