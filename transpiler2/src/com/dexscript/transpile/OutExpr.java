package com.dexscript.transpile;

import com.dexscript.ast.expr.*;
import com.dexscript.resolve.Denotation;
import com.dexscript.resolve.ResolveValue;
import com.dexscript.transpile.gen.Gen;

public class OutExpr {

    private final OutCtor oCtor;
    private String val;
    private final Gen g;

    public OutExpr(OutCtor oCtor, DexExpr iExpr) {
        this.oCtor = oCtor;
        g = new Gen(oCtor.indention());
        if (iExpr instanceof DexStringLiteral) {
            gen((DexStringLiteral) iExpr);
        } else if (iExpr instanceof DexReference) {
            gen((DexReference)iExpr);
        } else if (iExpr instanceof DexAddExpr) {
            gen((DexAddExpr)iExpr);
        } else if (iExpr instanceof DexIntegerLiteral) {
            gen((DexIntegerLiteral)iExpr);
        } else {
            throw new UnsupportedOperationException("not implemented: " + iExpr.getClass());
        }
    }

    private void gen(DexIntegerLiteral iInt) {
        val = iInt.toString() + "L";
    }

    private void gen(DexAddExpr iAddExpr) {
        OutExpr oLeft = new OutExpr(oCtor, iAddExpr.left());
        OutExpr oRight = new OutExpr(oCtor, iAddExpr.right());
        val = "(" + oLeft + " + " + oRight + ")";
    }

    private void gen(DexReference iRef) {
        Denotation.Value dntVal = oCtor.township().resolveValue(iRef);
        OutField oField = dntVal.elem.attachmentOfType(OutField.class);
        val = oField.fieldName;
    }

    private void gen(DexStringLiteral iStr) {
        val = "\"" + iStr.src().slice(iStr.begin() + 1, iStr.end() - 1).toString() + "\"";
    }

    @Override
    public String toString() {
        return val;
    }
}
