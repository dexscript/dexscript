package com.dexscript.transpiler2;

import com.dexscript.parser2.expr.DexExpr;
import com.dexscript.parser2.expr.DexReference;
import com.dexscript.parser2.expr.DexStringLiteral;
import com.dexscript.transpiler2.gen.Gen;

public class OutExpr {

    private String val;
    private final Gen g;

    public OutExpr(String prefix, DexExpr iExpr) {
        g = new Gen(prefix);
        if (iExpr instanceof DexStringLiteral) {
            gen((DexStringLiteral) iExpr);
        } else if (iExpr instanceof DexReference) {
            gen((DexReference)iExpr);
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    private void gen(DexReference iRef) {
        iRef.resolve();
    }

    private void gen(DexStringLiteral iStr) {
        val = "\"" + iStr.src().slice(iStr.begin() + 1, iStr.end() - 1).toString() + "\"";
    }

    public String value() {
        return val;
    }
}
