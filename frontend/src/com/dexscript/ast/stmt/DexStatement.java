package com.dexscript.ast.stmt;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexStatement extends DexElement {

    protected DexStatement prev;

    public DexStatement(Text src) {
        super(src);
    }

    public final void reparent(DexElement parent, DexStatement prev) {
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

    public static DexStatement parse(Text src) {
        DexStatement stmt = new DexReturnStmt(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexBlock(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexIfStmt(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexShortVarDecl(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexVarDecl(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexAssignStmt(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexAwaitStmt(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexProduceStmt(src);
        if (stmt.matched()) {
            return stmt;
        }
        return new DexExprStmt(src);
    }

    public static DexStatement parse(String src) {
        return parse(new Text(src));
    }

    public DexReturnStmt asReturn() {
        return (DexReturnStmt) this;
    }

    public DexAwaitStmt asAwait() {
        return (DexAwaitStmt) this;
    }

    public DexProduceStmt asProduce() {
        return (DexProduceStmt) this;
    }
}
