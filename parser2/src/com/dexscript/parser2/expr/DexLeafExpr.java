package com.dexscript.parser2.expr;

public interface DexLeafExpr extends DexExpr {
    @Override
    default void walkDown(Visitor visitor) {
    }
}
