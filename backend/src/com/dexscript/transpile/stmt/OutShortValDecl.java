package com.dexscript.transpile.stmt;

import com.dexscript.ast.func.DexShortVarDecl;
import com.dexscript.denotation.Type;
import com.dexscript.transpile.OutCtor;
import com.dexscript.transpile.OutExpr;
import com.dexscript.transpile.OutField;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;

public class OutShortValDecl {

    public OutShortValDecl(OutCtor oCtor, Gen g, DexShortVarDecl iShortVarDecl) {
        Type exprType = oCtor.town().resolveType(iShortVarDecl.expr());
        OutField oField = oCtor.oClass().allocateField(iShortVarDecl.decls().get(0), exprType);
        iShortVarDecl.attach(oField);
        OutExpr oExpr = new OutExpr(oCtor, g, iShortVarDecl.expr());
        g.__(oField.fieldName
        ).__(" = "
        ).__(oExpr
        ).__(new Line(";"));
    }
}
