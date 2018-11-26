package com.dexscript.transpile.stmt;

import com.dexscript.ast.stmt.DexReturnStmt;
import com.dexscript.resolve.Denotation;
import com.dexscript.transpile.OutCtor;
import com.dexscript.transpile.OutExpr;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;

public class OutReturnStmt {

    public OutReturnStmt(OutCtor oCtor, Gen g, DexReturnStmt returnStmt) {
        Denotation.Type retType = oCtor.town().resolveType(oCtor.iFunc().sig().ret());
        OutExpr oExpr = new OutExpr(oCtor, g, returnStmt.expr());
        g.__("finish(("
        ).__(retType.javaClassName
        ).__(')'
        ).__(oExpr
        ).__(new Line(");"));
    }
}