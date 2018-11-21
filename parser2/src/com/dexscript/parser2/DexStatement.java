package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.expr.DexExpr;

public class DexStatement implements DexElement {

    private final Text src;
    private DexElement elem;

    public DexStatement(Text src) {
        this.src = src;
        elem = new DexShortVarDecl(src);
        if (elem.matched()) {
            return;
        }
        elem = DexExpr.parse(src, 0);
        if (elem.matched()) {
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
        return elem.begin();
    }

    @Override
    public int end() {
        return elem.end();
    }

    @Override
    public boolean matched() {
        return elem.matched();
    }

    @Override
    public DexError err() {
        return elem.err();
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    public DexExpr exprStmt() {
        if (elem instanceof DexExpr) {
            return (DexExpr) elem;
        }
        return null;
    }

    public DexShortVarDecl shortVarDecl() {
        if (elem instanceof DexShortVarDecl) {
            return (DexShortVarDecl) elem;
        }
        return null;
    }
}
