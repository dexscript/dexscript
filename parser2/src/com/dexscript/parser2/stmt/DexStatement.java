package com.dexscript.parser2.stmt;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.Text;
import com.dexscript.parser2.expr.DexExpr;

public interface DexStatement extends DexElement {

    static DexStatement parse(Text src) {
        DexStatement stmt = new DexReturnStmt(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexBlock(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexShortVarDecl(src);
        if (stmt.matched()) {
            return stmt;
        }
        return DexExpr.parse(src, 0);
    }

    static DexStatement parse(String src) {
        return parse(new Text(src));
    }
}
