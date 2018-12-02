package com.dexscript.ast.func;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;

public class DexExprStmt extends DexStatement {

    private final DexExpr expr;

    public DexExprStmt(Text src) {
        super(src);
        expr = DexExpr.parse(src);
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
    public DexSyntaxError syntaxError() {
        return expr.syntaxError();
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
