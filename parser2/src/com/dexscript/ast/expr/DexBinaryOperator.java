package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.func.DexStatement;

public abstract class DexBinaryOperator extends DexExpr {

    protected DexExpr left;
    protected DexExpr right;

    public DexBinaryOperator(Text src) {
        super(src);
    }

    public final DexExpr left() {
        return left;
    }

    public final DexExpr right() {
        return right;
    }

    @Override
    public final void walkDown(Visitor visitor) {
        visitor.visit(left());
        visitor.visit(right());
    }

    public final void reparent(DexElement parent, DexStatement stmt) {
        super.reparent(parent, stmt);
        if (left() != null) {
            left().reparent(this, stmt);
        }
        if (right() != null) {
            right().reparent(this, stmt);
        }
    }
}
