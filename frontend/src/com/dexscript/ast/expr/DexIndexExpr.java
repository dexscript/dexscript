package com.dexscript.ast.expr;

import com.dexscript.ast.core.Text;

public class DexIndexExpr extends DexExpr implements DexInvocationExpr {

    public DexIndexExpr(Text src) {
        super(src);
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int end() {
        return 0;
    }

    @Override
    public boolean matched() {
        return false;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }

    @Override
    public DexInvocation invocation() {
        return null;
    }
}
