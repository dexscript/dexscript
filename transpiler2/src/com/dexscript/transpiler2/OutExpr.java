package com.dexscript.transpiler2;

import com.dexscript.parser2.expr.DexExpr;
import com.dexscript.parser2.expr.DexStringLiteral;
import com.dexscript.transpiler2.gen.Gen;

public class OutExpr {

    private String val;
    private final Gen g;

    public OutExpr(String prefix, DexExpr iExpr) {
        g = new Gen(prefix);
        if (iExpr instanceof DexStringLiteral) {
            gen((DexStringLiteral) iExpr);
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    private void gen(DexStringLiteral iExpr) {
        val = "\"" + iExpr.src().slice(iExpr.begin() + 1, iExpr.end() - 1).toString() + "\"";
    }

    public String value() {
        return val;
    }
}
