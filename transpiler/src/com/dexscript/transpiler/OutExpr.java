package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OutExpr extends OutValue {

    private OutMethod oMethod;
    private OutClass oClass;

    OutExpr(OutMethod oMethod, DexExpression iExpr) {
        super(oMethod.iFile());
        this.oMethod = oMethod;
        oClass = oMethod.oClass();
        iExpr.accept(this);
        if (type == null) {
            throw new IllegalStateException("failed to translate expression: " + iExpr.getNode().getText());
        }
    }

    public String expectOne() {
        if (type.kind != RuntimeTypeKind.RESULT) {
            return toString();
        }
        return "((Result1)" + toString() + ").result1__()";
    }

    @Override
    public void visitLiteral(@NotNull DexLiteral iLit) {
        if (iLit.getInt() != null) {
            genIntLiteral(iLit.getInt());
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    private void genIntLiteral(PsiElement iIntLit) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Long");
        append("((Long)");
        append(iIntLit);
        append("L)");
    }

    @Override
    public void visitStringLiteral(@NotNull DexStringLiteral iStrLit) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "String");
        String text = iStrLit.getNode().getText();
        append('"');
        append(text.substring(1, text.length() - 1));
        append('"');
    }

    @Override
    public void visitNewExpr(@NotNull DexNewExpr iNewExpr) {
        type = RuntimeType.RESULT;
        String funcName = iNewExpr.getExpression().getNode().getText();
        oClass.referenced(iNewExpr);
        append(oClass.shimClassName());
        append('.');
        append(funcName);
        append("()");
    }

    @Override
    public void visitCallExpr(@NotNull DexCallExpr iCallExpr) {
        List<OutExpr> oArgs = new ArrayList<>();
        for (DexExpression iArg : iCallExpr.getCallExprArgs().getExpressionList()) {
            oArgs.add(new OutExpr(oMethod, iArg));
        }
        type = RuntimeType.RESULT;
        String symbolName = iCallExpr.getExpression().getNode().getText();
        oClass.referenced(iCallExpr);
        String[] parts = symbolName.split("\\.");
        String funcName = parts[parts.length - 1];
        String fieldName = oMethod.oClass().addField(funcName, "Result");
        oMethod.append(fieldName);
        oMethod.append(" = ");
        oMethod.append(oMethod.oClass().shimClassName());
        oMethod.append('.');
        oMethod.append(funcName);
        oMethod.append('(');
        boolean isFirst = true;
        if (parts.length == 1) {
        } else if (parts.length == 2) {
            oMethod.append(parts[0]);
            isFirst = false;
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
        for (OutExpr oArg : oArgs) {
            if (isFirst) {
                isFirst = false;
            } else {
                oMethod.append(", ");
            }
            oMethod.append(oArg);
        }
        oMethod.appendNewLine(");");
        append(fieldName);
    }

    @Override
    public void visitAddExpr(@NotNull DexAddExpr iAddExpr) {
        type = RuntimeType.RESULT;
        String funcName = "Add__";
        oClass.referenced(iAddExpr);
        String fieldName = oClass.addField("addResult", "Result");
        String oLeftExpr = new OutExpr(oMethod, iAddExpr.getLeft()).expectOne();
        String oRightExpr = new OutExpr(oMethod, iAddExpr.getRight()).expectOne();
        oMethod.append(fieldName);
        oMethod.append(" = ");
        oMethod.append(oClass.shimClassName());
        oMethod.append('.');
        oMethod.append(funcName);
        oMethod.append('(');
        oMethod.append(oLeftExpr);
        oMethod.append(',');
        oMethod.append(oRightExpr);
        oMethod.appendNewLine(");");
        append(fieldName);
    }

    @Override
    public void visitUnaryExpr(@NotNull DexUnaryExpr iExpr) {
        if (iExpr.getGetResult() != null) {
            genGetResult(iExpr.getExpression());
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }

    @Override
    public void visitReferenceExpression(@NotNull DexReferenceExpression iRefExpr) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Object");
        append(iRefExpr);
    }

    private void genGetResult(DexExpression iExpr) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Object");
        append("((Result1)");
        append(new OutExpr(oMethod, iExpr));
        append(").result1__()");
    }

    @Override
    public void visitCastExpr(@NotNull DexCastExpr iCastExpr) {
        type = RuntimeType.RESULT;
        String funcName = "Cast__";
        oClass.referenced(iCastExpr);
        String fieldName = oClass.addField("castResult", "Result");
        OutExpr oCastFrom = new OutExpr(oMethod, iCastExpr.getExpression());
        oMethod.append(fieldName);
        oMethod.append(" = ");
        oMethod.append(oClass.shimClassName());
        oMethod.append('.');
        oMethod.append(funcName);
        oMethod.append('(');
        oMethod.append(oCastFrom);
        oMethod.append(", ");
        oMethod.append(TransType.translateType(iCastExpr.getType()).className);
        oMethod.appendNewLine(".class);");
        append(fieldName);
    }
}
