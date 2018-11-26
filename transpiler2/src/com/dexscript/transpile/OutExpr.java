package com.dexscript.transpile;

import com.dexscript.ast.expr.*;
import com.dexscript.resolve.BuiltinTypes;
import com.dexscript.resolve.Denotation;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;

public class OutExpr {

    private final OutCtor oCtor;
    private final Town town;
    private final Gen g;
    private final Gen val = new Gen();

    public OutExpr(OutCtor oCtor, Gen g, DexExpr iExpr) {
        this.oCtor = oCtor;
        this.g = g;
        town = oCtor.town();
        if (iExpr instanceof DexStringLiteral) {
            gen((DexStringLiteral) iExpr);
        } else if (iExpr instanceof DexReference) {
            gen((DexReference) iExpr);
        } else if (iExpr instanceof DexAddExpr) {
            gen((DexAddExpr) iExpr);
        } else if (iExpr instanceof DexIntegerLiteral) {
            gen((DexIntegerLiteral) iExpr);
        } else if (iExpr instanceof DexCallExpr) {
            gen((DexCallExpr) iExpr);
        } else {
            throw new UnsupportedOperationException("not implemented: " + iExpr.getClass());
        }
    }

    private void gen(DexCallExpr iCallExpr) {
        Denotation.Type functionType = town.resolveFunction(iCallExpr.target().asRef());
        OutField oField = oCtor.oClass().allocateField(iCallExpr.target(), BuiltinTypes.RESULT_TYPE);
        Boat boat = functionType.referenced().attachmentOfType(Boat.class);
        g.__(oField.fieldName
        ).__(" = "
        ).__(boat.applyF()
        ).__(new Line("();"));
        val.__('('
        ).__(oField.fieldName
        ).__(".value())");
    }

    private void gen(DexIntegerLiteral iInt) {
        val.__(iInt).__('L');
    }

    private void gen(DexAddExpr iAddExpr) {
        OutExpr oLeft = new OutExpr(oCtor, g, iAddExpr.left());
        OutExpr oRight = new OutExpr(oCtor, g, iAddExpr.right());
        val.__('('
        ).__(oLeft
        ).__(" + "
        ).__(oRight
        ).__(')');
    }

    private void gen(DexReference iRef) {
        Denotation.Value dntVal = oCtor.town().resolveValue(iRef);
        OutField oField = dntVal.referenced().attachmentOfType(OutField.class);
        val.__(oField.fieldName);
    }

    private void gen(DexStringLiteral iStr) {
        val.__('"'
        ).__(iStr.src().slice(iStr.begin() + 1, iStr.end() - 1)
        ).__('"');
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
