package com.dexscript.type.core;

import com.dexscript.ast.expr.DexStringConst;

import java.util.Objects;

public class StringConstType implements DType {

    static {
        InferType.register(DexStringConst.class, (ts, localTypeTable, elem) -> ts.constOf(elem.constValue()));
    }

    private final TypeSystem ts;
    private final String val;

    public StringConstType(TypeSystem ts, String val) {
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

    public String constValue() {
        return val;
    }

    @Override
    public String toString() {
        return "(const)'" + val + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringConstType that = (StringConstType) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts, val);
    }
}
