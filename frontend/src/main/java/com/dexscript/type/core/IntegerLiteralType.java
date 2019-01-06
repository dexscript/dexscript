package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexIntegerLiteralType;

import java.util.HashMap;
import java.util.Objects;

public class IntegerLiteralType implements DType {

    static {
        InferType.handlers.putAll(new HashMap<Class<? extends DexElement>, InferType>() {{
            put(DexIntegerLiteralType.class, (ts, localTypeTable, elem) ->
                    new IntegerLiteralType(ts, elem.toString()));
        }});
    }

    private final TypeSystem ts;
    private final String val;

    public IntegerLiteralType(TypeSystem ts, String val) {
        this.ts = ts;
        this.val = val;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        if (that instanceof IntegerConstType) {
            return val.equals(((IntegerConstType) that).constValue());
        }
        return that.equals(this);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    public String literalValue() {
        return val;
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerLiteralType that = (IntegerLiteralType) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ts, val);
    }
}
