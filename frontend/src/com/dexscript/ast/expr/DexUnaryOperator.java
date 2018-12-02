package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.func.DexStatement;

public abstract class DexUnaryOperator extends DexExpr {

    protected DexExpr right;

    public DexUnaryOperator(Text src) {
        super(src);
    }

    public final DexExpr right() {
        return right;
    }


    @Override
    public final int begin() {
        return src.begin;
    }

    @Override
    public final int end() {
        return right().end();
    }

    @Override
    public final boolean matched() {
        return right() != null && right().matched();
    }

    @Override
    public final void walkDown(Visitor visitor) {
        if (right() != null) {
            visitor.visit(right());
        }
    }

    public final void reparent(DexElement parent, DexStatement stmt) {
        super.reparent(parent, stmt);
        if (right() != null) {
            right().reparent(this, stmt);
        }
    }
}
