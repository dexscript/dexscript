package com.dexscript.transpile.body;

import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutDiscardMethod;
import com.dexscript.type.DType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;

public class TranslateReturn implements Translate<DexReturnStmt> {

    @Override
    public void handle(OutClass oClass, DexReturnStmt iReturnStmt) {
        TypeSystem ts = oClass.typeSystem();
        DType retType = ResolveType.$(ts, null, iReturnStmt.sig().ret());
        String val = Translate.translateExpr(oClass, iReturnStmt.expr(), retType);
        oClass.g().__("produce("
        ).__(val
        ).__(new Line(");"));
        // discard following statements to avoid javac compiler complaining "unreachable"
        new OutDiscardMethod(oClass);
    }
}
