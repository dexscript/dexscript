package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexBoolLiteralType;
import com.dexscript.ast.type.DexIntegerLiteralType;

import java.util.HashMap;
import java.util.Objects;

class BoolLiteralType implements DType {

    static {
        InferType.handlers.putAll(new HashMap<Class<? extends DexElement>, InferType>() {{
            put(DexBoolLiteralType.class, (ts, localTypeTable, elem) ->
                    new BoolLiteralType(ts, elem.toString()));
        }});
    }

    private final TypeSystem ts;
    private final String val;

    BoolLiteralType(TypeSystem ts, String val) {
        this.ts = ts;
        this.val = val;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof BoolConstType) {
            return val.equals(((BoolConstType) that).constValue());
        }
        return this.equals(that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoolLiteralType that = (BoolLiteralType) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts, val);
    }
}
