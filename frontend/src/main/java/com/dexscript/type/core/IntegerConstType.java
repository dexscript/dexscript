package com.dexscript.type.core;

import com.dexscript.ast.expr.DexIntegerConst;

import java.util.Objects;

public class IntegerConstType implements DType {

    static {
        InferType.register(DexIntegerConst.class, (ts, localTypeTable, elem) -> ts.constOfInteger(elem.toString()));
    }

    private final TypeSystem ts;
    private final String val;

    public IntegerConstType(TypeSystem ts, String val) {
        this.ts = ts;
        this.val = val;
    }

    public static void init() {
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        return false;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return "(const)" + val;
    }

    public String constValue() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerConstType that = (IntegerConstType) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts, val);
    }
}
