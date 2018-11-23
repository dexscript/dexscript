package com.dexscript.parser2.expr;

public interface DexBinaryOperator extends DexExpr {

    DexExpr left();

    DexExpr right();

    static DexExpr left(DexExpr expr) {
        return ((DexBinaryOperator) expr).left();
    }

    static DexExpr right(DexExpr expr) {
        return ((DexBinaryOperator) expr).right();
    }

    @Override
    default void walkDown(Visitor visitor) {
        visitor.visit(left());
        visitor.visit(right());
    }
}
