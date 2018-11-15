package com.dexscript.transpiler;

import com.dexscript.psi.*;

import java.util.List;

public class OutShimClass extends OutClass {

    public OutShimClass(DexFile iFile, String packageName, List<OutClass> oClasses) {
        super(iFile, packageName, oClasses.get(0).shimClassName());
        appendNewLine("import com.dexscript.runtime.Result;");
        appendNewLine();
        append("public class ");
        append(className());
        append(" {");
        indent(() -> {
            for (OutClass tClass : oClasses) {
                for (DexExpression ref : tClass.references()) {
                    if (ref instanceof DexCallExpr) {
                        DexCallExpr callExpr = (DexCallExpr) ref;
                        genShim4CallExpr(callExpr, callExpr.getExpression().getNode().getText());
                    } else if (ref instanceof DexNewExpr) {
                        DexNewExpr newExpr = (DexNewExpr) ref;
                        genShim4CallExpr(ref, newExpr.getExpression().getNode().getText());
                    } else if (ref instanceof DexAddExpr) {
                        genShim4Add((DexAddExpr) ref);
                    } else if (ref instanceof DexCastExpr) {
                        genShim4Cast((DexCastExpr) ref);
                    } else {
                        throw new UnsupportedOperationException("not implemented");
                    }
                }
            }
        });
        append("}");
        appendNewLine();
    }

    private void genShim4Cast(DexCastExpr iCastExpr) {
        appendSourceLine(iCastExpr);
        appendNewLine("public static Result Cast__(Object castFrom, Object castToType) {");
        appendNewLine("  return com.dexscript.runtime.Cast.Cast__(castFrom, castToType);");
        appendNewLine("}");
    }

    private void genShim4Add(DexAddExpr iAddExpr) {
        appendSourceLine(iAddExpr);
        appendNewLine("public static Result Add__(Object left, Object right) {");
        appendNewLine("  return com.dexscript.runtime.Math.Add__(left, right);");
        appendNewLine("}");
    }

    private void genShim4CallExpr(DexExpression iCallExpr, String symbolName) {
        String[] parts = symbolName.split("\\.");
        String funcName = parts[parts.length - 1];
        appendSourceLine(iCallExpr);
        append("public static Result ");
        append(funcName);
        if (parts.length == 1) {
            append("() {");
            indent(() -> {
                append("return new ");
                append(symbolName);
                append("();");
            });
            appendNewLine("}");
        } else if (parts.length == 2) {
            append("(Object obj) {");
            indent(() -> {
                append("return ((World)obj).");
                append(funcName);
                append("();");
            });
            appendNewLine("}");
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
