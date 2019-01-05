package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.expr.DexBoolConst;

import java.util.HashMap;
import java.util.Objects;

class BoolConstType implements DType {

    static {
        InferType.handlers.putAll(new HashMap<Class<? extends DexElement>, InferType>() {{
            put(DexBoolConst.class, (ts, elem) -> ts.constOfBool(elem.toString()));
        }});
    }

    private final TypeSystem ts;
    private final String val;

    BoolConstType(TypeSystem ts, String val) {
        this.ts = ts;
        this.val = val;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoolConstType that = (BoolConstType) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts, val);
    }

    public String constValue() {
        return val;
    }
}
