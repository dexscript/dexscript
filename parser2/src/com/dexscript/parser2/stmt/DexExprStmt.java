package com.dexscript.parser2.stmt;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.expr.DexExpr;

public class DexExprStmt implements DexStatement {

    private final DexExpr expr;

    // for walk up
    private DexElement parent;
    private DexStatement prev;

    public DexExprStmt(Text src) {
        expr = DexExpr.parse(src);
    }

    @Override
    public void reparent(DexElement parent, DexStatement prev) {
        this.parent = parent;
        this.prev = prev;
        expr.reparent(this, this);
    }

    @Override
    public DexStatement prev() {
        return prev;
    }

    @Override
    public Text src() {
        return expr.src();
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
    public DexElement parent() {
        return parent;
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
