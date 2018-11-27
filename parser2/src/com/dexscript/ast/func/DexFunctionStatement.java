package com.dexscript.ast.func;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexFunctionStatement extends DexElement {

    protected DexFunctionStatement prev;

    public DexFunctionStatement(Text src) {
        super(src);
    }

    public final void reparent(DexElement parent, DexFunctionStatement prev) {
        this.parent = parent;
        this.prev = prev;
    }

    @Override
    public final DexElement prev() {
        if (prev != null) {
            return prev;
        } else {
            return parent();
        }
    }

    public static DexFunctionStatement parse(Text src) {
        DexFunctionStatement stmt = new DexReturnStmt(src);
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

    public static DexFunctionStatement parse(String src) {
        return parse(new Text(src));
    }

    public DexReturnStmt asReturn() {
        return (DexReturnStmt) this;
    }
}
