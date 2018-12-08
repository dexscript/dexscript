package com.dexscript.ast.stmt;

import com.dexscript.ast.core.Text;

public abstract class DexSimpleStatement extends DexStatement {

    public DexSimpleStatement(Text src) {
        super(src);
    }

    public static DexSimpleStatement parse(Text src) {
        DexSimpleStatement stmt = new DexShortVarDecl(src);
        if (stmt.matched()) {
            return stmt;
        }
        stmt = new DexAssignStmt(src);
        if (stmt.matched()) {
            return stmt;
        }
        return new DexExprStmt(src);
    }
}
