package com.dexscript.transpile.body;

import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.stmt.DexShortVarDecl;
import com.dexscript.gen.Line;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutField;

public class TranslateShortVarDecl implements Translate<DexShortVarDecl> {

    @Override
    public void handle(OutClass oClass, DexShortVarDecl iShortVarDecl) {
        if (iShortVarDecl.decls().size() != 1) {
            throw new UnsupportedOperationException("not implemented: destruction assignment");
        }
        DexIdentifier decl = iShortVarDecl.decls().get(0);
        DexExpr expr = iShortVarDecl.expr();
        Translate.$(oClass, expr);
        OutField oField = oClass.allocateField(decl.toString());
        oClass.g().__(oField.value()
        ).__(" = "
        ).__(OutValue.of(expr)
        ).__(new Line(";"));
        decl.attach(oField);
    }
}
