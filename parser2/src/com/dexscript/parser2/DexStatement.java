package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.expr.DexExpr;

public class DexStatement implements DexElement {

    private final Text src;
    private DexElement matched;

    public DexStatement(Text src) {
        this.src = src;
        matched = DexExpr.parse(src, 0);
        if (matched.matched()) {
            return;
        }
    }

    public DexStatement(String src) {
        this(new Text(src));
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        return matched.begin();
    }

    @Override
    public int end() {
        return matched.end();
    }

    @Override
    public boolean matched() {
        return matched.matched();
    }

    @Override
    public DexError err() {
        return matched.err();
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    public DexExpr exprStmt() {
        if (matched instanceof DexExpr) {
            return (DexExpr) matched;
        }
        return null;
    }
}
