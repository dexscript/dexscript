package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayType extends TopLevelType implements GenericType, FunctionsProvider {

    private final static List<Type> TYPE_PARAMETERS = Arrays.asList(BuiltinTypes.ANY);
    private final TopLevelTypeTable typeTable;
    private final FunctionTable funcTable;
    private List<FunctionType> members;

    public ArrayType(@NotNull TopLevelTypeTable typeTable, @NotNull FunctionTable funcTable) {
        this(typeTable, funcTable, TYPE_PARAMETERS);
    }

    public ArrayType(TopLevelTypeTable typeTable, FunctionTable funcTable, List<Type> typeArgs) {
        super("Array", "Object[]");
        this.typeTable = typeTable;
        this.funcTable = funcTable;
        typeTable.define(this);
        funcTable.lazyDefine(this);
        Type elem = typeArgs.get(0);
        members = Arrays.asList(getFunc(elem), setFunc(elem));
    }

    private FunctionType getFunc(Type elem) {
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        params.add(BuiltinTypes.INT64);
        return new FunctionType("Get__", params, elem);
    }

    private FunctionType setFunc(Type elem) {
        ArrayList<Type> params = new ArrayList<>();
        params.add(this);
        params.add(BuiltinTypes.INT64);
        params.add(elem);
        return new FunctionType("Set__", params, BuiltinTypes.VOID);
    }

    @Override
    public List<FunctionType> functions() {
        return members;
    }

    @Override
    public Type generateType(List<Type> typeArgs) {
        return new ArrayType(typeTable, funcTable, typeArgs);
    }

    @Override
    public List<Type> typeParameters() {
        return TYPE_PARAMETERS;
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        if (super.isAssignableFrom(that)) {
            return true;
        }
        return funcTable.isAssignableFrom(this, that);
    }
}
