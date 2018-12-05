package com.dexscript.transpile.elem;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.func.DexShortVarDecl;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutValue;

public class TranslateShortVarDecl implements TranslateElem {

    @Override
    public void handle(OutClass oClass, DexElement iElem) {
        DexShortVarDecl iShortVarDecl = (DexShortVarDecl) iElem;
        if (iShortVarDecl.decls().size() != 1) {
            throw new UnsupportedOperationException("not implemented: destruction assignment");
        }
        DexIdentifier decl = iShortVarDecl.decls().get(0);
        DexExpr expr = iShortVarDecl.expr();
        TranslateElem.$(oClass, expr);
        decl.attach(expr.attachmentOfType(OutValue.class));
    }
}
