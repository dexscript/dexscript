package com.dexscript.type;

public class Int64Type extends NamedType {

    public Int64Type() {
        super("int64", "java.lang.Long");
    }

    @Override
    public String toString() {
        return "int64";
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        if (super.isAssignableFrom(that)) {
            return true;
        }
        if (that instanceof Int64Type) {
            return true;
        }
        if (that instanceof IntegerLiteralType) {
            try {
                Long.valueOf(((IntegerLiteralType) that).literalValue());
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String initValue() {
        return "0L";
    }
}
