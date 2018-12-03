package com.dexscript.transpile;

import com.dexscript.ast.expr.*;
import com.dexscript.denotation.Value;
import com.dexscript.resolve.Boat;
import com.dexscript.denotation.BuiltinTypes;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;

import java.util.ArrayList;
import java.util.List;

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
        } else if (iExpr instanceof DexFunctionCallExpr) {
            gen((DexFunctionCallExpr) iExpr);
        } else if (iExpr instanceof DexMethodCallExpr) {
            gen((DexMethodCallExpr) iExpr);
        } else if (iExpr instanceof DexNewExpr) {
            gen((DexNewExpr) iExpr);
        }  else if (iExpr instanceof DexConsumeExpr) {
            gen((DexConsumeExpr) iExpr);
        } else {
            throw new UnsupportedOperationException("not implemented: " + iExpr.getClass());
        }
    }

    private void gen(DexConsumeExpr iGetResultExpr) {
        OutExpr oExpr = new OutExpr(oCtor, g, iGetResultExpr.right());
        val.__("(((Result)"
        ).__(oExpr.toString()
        ).__(").value())");
    }

    private void gen(DexNewExpr iNewExpr) {
        Denotation.FunctionType functionType = town.resolveFunction(iNewExpr);
        List<DexExpr> iArgs = iNewExpr.args();
        List<OutExpr> args = new ArrayList<>();
        for (DexExpr arg : iArgs) {
            args.add(new OutExpr(oCtor, g, arg));
        }
        OutField oField = oCtor.oClass().allocateField(iNewExpr.target().asRef(), BuiltinTypes.RESULT_TYPE);
        Boat boat = functionType.boat();
        g.__(oField.fieldName
        ).__(" = "
        ).__(boat.applyF()
        ).__('(');
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            OutExpr arg = args.get(i);
            g.__(arg);
        }
        g.__(new Line(");"));
        val.__(oField.fieldName);
    }

    private void gen(DexMethodCallExpr iCallExpr) {
        List<DexExpr> iArgs = new ArrayList<>();
        iArgs.add(iCallExpr.obj());
        iArgs.addAll(iCallExpr.args());
        apply(town.resolveFunction(iCallExpr), iCallExpr.method(), iArgs);
    }

    private void gen(DexFunctionCallExpr iCallExpr) {
        Denotation.FunctionType functionType = town.resolveFunction(iCallExpr);
        apply(functionType, iCallExpr.target().asRef(), iCallExpr.args());
    }

    private void apply(Denotation.FunctionType functionType, DexReference functionRef, List<DexExpr> iArgs) {
        List<OutExpr> args = new ArrayList<>();
        for (DexExpr arg : iArgs) {
            args.add(new OutExpr(oCtor, g, arg));
        }
        OutField oField = oCtor.oClass().allocateField(functionRef, BuiltinTypes.RESULT_TYPE);
        Boat boat = functionType.boat();
        g.__(oField.fieldName
        ).__(" = "
        ).__(boat.applyF()
        ).__('(');
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            OutExpr arg = args.get(i);
            g.__(arg);
        }
        g.__(new Line(");"));
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
        Value dntVal = oCtor.town().resolveValue(iRef);
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
