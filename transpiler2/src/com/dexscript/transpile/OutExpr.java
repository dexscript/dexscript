package com.dexscript.transpile;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.expr.DexStringLiteral;
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
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    private void gen(DexReference iRef) {
        Denotation.Value dntVal = oCtor.township().resolveValue(iRef);
        OutField oField = dntVal.elem.attachmentOfType(OutField.class);
        val = oField.fieldName;
    }

    private void gen(DexStringLiteral iStr) {
        val = "\"" + iStr.src().slice(iStr.begin() + 1, iStr.end() - 1).toString() + "\"";
    }

    public String value() {
        return val;
    }
}
