package com.dexscript.transpile;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.expr.DexStringLiteral;
import com.dexscript.resolve.ResolveValue;
import com.dexscript.transpile.gen.Gen;

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
        new ResolveValue().__(iRef);
    }

    private void gen(DexStringLiteral iStr) {
        val = "\"" + iStr.src().slice(iStr.begin() + 1, iStr.end() - 1).toString() + "\"";
    }

    public String value() {
        return val;
    }
}
