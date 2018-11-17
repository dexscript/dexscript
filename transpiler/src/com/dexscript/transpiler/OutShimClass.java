package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.dexscript.runtime.DexScriptException;
import com.intellij.psi.ResolveResult;

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
                        DexCallExpr iCallExpr = (DexCallExpr) ref;
                        genShim4CallExpr(iCallExpr, (DexReferenceExpression) iCallExpr.getExpression());
                    } else if (ref instanceof DexNewExpr) {
                        DexNewExpr newExpr = (DexNewExpr) ref;
                        genShim4CallExpr(ref, ((DexReferenceExpression) newExpr.getExpression()));
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

    private void genShim4CallExpr(DexExpression iCallExpr, DexReferenceExpression iRefExpr) {
        String symbolName = iRefExpr.getIdentifier().getNode().getText();
        ResolveResult[] resolved = iRefExpr.getReference().multiResolve(false);
        if (resolved.length == 0) {
            throw DexScriptException.reportMissingImplementation(symbolName);
        }
        if (resolved.length > 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        String[] parts = symbolName.split("\\.");
        String funcName = parts[parts.length - 1];
        appendSourceLine(iCallExpr);
        append("public static Result ");
        append(funcName);
        if (parts.length == 1) {
            append('(');
            DexFunctionDeclaration iFuncDecl = (DexFunctionDeclaration) resolved[0].getElement();
            List<DexParameterDeclaration> iParams = iFuncDecl.getSignature().getParameters().getParameterDeclarationList();
            appendParamDef(iParams);
            append(") {");
            indent(() -> {
                append("return new ");
                append(symbolName);
                append('(');
                appendParamInvocation(iParams);
                append(");");
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

    private void appendParamDef(List<DexParameterDeclaration> iParams) {
        boolean isFirst = true;
        for (DexParameterDeclaration iParam : iParams) {
            for (DexParamDefinition iParamDef : iParam.getParamDefinitionList()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    append(", ");
                }
                append("Object ");
                append(iParamDef.getIdentifier());
            }
        }
    }

    private void appendParamInvocation(List<DexParameterDeclaration> iParams) {
        boolean isFirst = true;
        for (DexParameterDeclaration iParam : iParams) {
            for (DexParamDefinition iParamDef : iParam.getParamDefinitionList()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    append(", ");
                }
                append(iParamDef.getIdentifier());
            }
        }
    }
}
