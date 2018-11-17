package com.dexscript.transpiler;

import com.dexscript.psi.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutShimClass extends OutClass {

    private final Set<String> generated = new HashSet<String>();

    public OutShimClass(DexFile iFile, String packageName, List<OutClass> oClasses) {
        super(iFile, packageName, oClasses.get(0).shimClassName());
        appendNewLine("import com.dexscript.runtime.*;");
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
        String sig = "Result Cast__(Object castFrom, Object castToType)";
        if (generated.contains(sig)) {
            return;
        }
        generated.add(sig);
        appendSourceLine(iCastExpr);
        append("public static ");
        append(sig);
        appendNewLine(" {");
        appendNewLine("  return com.dexscript.runtime.Cast.Cast__(castFrom, castToType);");
        appendNewLine("}");
    }

    private void genShim4Add(DexAddExpr iAddExpr) {
        String sig = "Add__(Object left, Object right)";
        if (generated.contains(sig)) {
            return;
        }
        generated.add(sig);
        appendSourceLine(iAddExpr);
        append("public static Result ");
        append(sig);
        appendNewLine(" {");
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
