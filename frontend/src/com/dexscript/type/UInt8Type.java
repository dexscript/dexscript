package com.dexscript.type;

public class UInt8Type extends NamedType {

    public UInt8Type() {
        super("uint8", "com.dexscript.runtime.UInt8");
    }

    @Override
    public String toString() {
        return "uint8";
    }

    @Override
    protected boolean isSubType(TypeComparisonContext ctx, Type that) {
        if (that instanceof UInt8Type) {
            return true;
        }
        return false;
    }
}
