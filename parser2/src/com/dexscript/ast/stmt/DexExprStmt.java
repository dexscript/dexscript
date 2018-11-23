package com.dexscript.ast.stmt;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.DexError;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;

public class DexExprStmt extends DexStatement {

    private final DexExpr expr;

    public DexExprStmt(Text src) {
        super(src);
        expr = DexExpr.parse(src);
    }

    @Override
    public void reparent(DexElement parent, DexStatement prev) {
        this.parent = parent;
        this.prev = prev;
        expr.reparent(this, this);
    }

    @Override
    public int begin() {
        return expr.begin();
    }

    @Override
    public int end() {
        return expr.end();
    }

    @Override
    public boolean matched() {
        return expr.matched();
    }

    @Override
    public DexError err() {
        return expr.err();
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(expr);
    }

    @Override
    public String toString() {
        return expr.toString();
    }
}
