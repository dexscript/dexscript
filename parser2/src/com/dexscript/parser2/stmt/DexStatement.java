package com.dexscript.parser2.stmt;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.Text;

public interface DexStatement extends DexElement {

    void reparent(DexElement parent, DexStatement prev);

    DexStatement prev();

    @Override
    default void walkUp(Visitor visitor) {
        if (prev() != null) {
            visitor.visit(prev());
        } else {
            visitor.visit(parent());
        }
    }

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
        return new DexExprStmt(src);
    }

    static DexStatement parse(String src) {
        return parse(new Text(src));
    }
}
