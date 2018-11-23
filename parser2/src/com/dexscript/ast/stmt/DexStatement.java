package com.dexscript.ast.stmt;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexStatement extends DexElement {

    protected DexStatement prev;

    public DexStatement(Text src) {
        super(src);
    }

    public abstract void reparent(DexElement parent, DexStatement prev);

    public final DexStatement prev() {
        return prev;
    }

    @Override
    public final void walkUp(Visitor visitor) {
        if (prev() != null) {
            visitor.visit(prev());
        } else {
            visitor.visit(parent());
        }
    }

    public static DexStatement parse(Text src) {
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

    public static DexStatement parse(String src) {
        return parse(new Text(src));
    }
}
