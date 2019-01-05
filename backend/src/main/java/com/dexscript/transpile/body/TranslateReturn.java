package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.ResolveType;
import com.dexscript.type.core.TypeSystem;

public class TranslateReturn implements Translate<DexReturnStmt> {

    @Override
    public void handle(OutClass oClass, DexReturnStmt iReturnStmt) {
        TypeSystem ts = oClass.typeSystem();
        DType retType = ResolveType.$(ts, null, iReturnStmt.enclosingSig().ret());
        String val = Translate.translateExpr(oClass, iReturnStmt.expr(), retType);
        // if (true) to workaround unreachable statement
        oClass.g().__("if (true) { produce("
        ).__(val
        ).__(new Line("); return; }"));
    }
}
