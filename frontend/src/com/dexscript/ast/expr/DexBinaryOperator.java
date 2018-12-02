package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.func.DexStatement;

public abstract class DexBinaryOperator extends DexExpr {

    protected DexExpr left;
    protected DexExpr right;

    public DexBinaryOperator(Text src, DexExpr left) {
        super(src);
        this.left = left;
    }

    public final DexExpr left() {
        return left;
    }

    public final DexExpr right() {
        return right;
    }

    @Override
    public final int begin() {
        return left().begin();
    }

    @Override
    public final int end() {
        return right().end();
    }

    @Override
    public final boolean matched() {
        return right != null && right.matched();
    }

    @Override
    public final void walkDown(Visitor visitor) {
        if (left() != null) {
            visitor.visit(left());
        }
        if (right() != null) {
            visitor.visit(right());
        }
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
