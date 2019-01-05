package com.dexscript.infer;

import com.dexscript.ast.stmt.DexForStmt;
import com.dexscript.ast.stmt.DexSimpleStatement;
import com.dexscript.type.core.TypeSystem;

public class InferForStmt implements InferValue<DexForStmt> {
    @Override
    public void handle(TypeSystem ts, DexForStmt forStmt, ValueTable table) {
        DexSimpleStatement initStmt = forStmt.initStmt();
        if (initStmt == null) {
            return;
        }
        InferValue inferValue = handlers.get(initStmt.getClass());
        if (inferValue == null) {
            return;
        }
        inferValue.handle(ts, initStmt, table);
    }
}
