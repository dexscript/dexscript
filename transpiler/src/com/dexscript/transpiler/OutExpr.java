package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public void visitLiteral(@NotNull DexLiteral o) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Long");
        append("((long)");
        append(o.getNode().getText());
        append(')');
    }

    @Override
    public void visitStringLiteral(@NotNull DexStringLiteral o) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "String");
        String text = o.getNode().getText();
        append('"');
        append(text.substring(1, text.length() - 1));
        append('"');
    }

    @Override
    public void visitNewExpr(@NotNull DexNewExpr iNewExpr) {
        type = new RuntimeType(RuntimeTypeKind.GENERIC_OBJECT, "Result");
        String funcName = iNewExpr.getExpression().getNode().getText();
        oClass.referenced(iNewExpr);
        append(oClass.shimClassName());
        append('.');
        append(funcName);
        append("()");
    }

    @Override
    public void visitCallExpr(@NotNull DexCallExpr o) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Result");
        String funcName = o.getExpression().getNode().getText();
        oClass.referenced(o);
        String fieldName = oMethod.oClass().addField(funcName, "Result");
        oMethod.append(fieldName);
        oMethod.append(" = ");
        oMethod.append(oMethod.oClass().shimClassName());
        oMethod.append('.');
        oMethod.append(funcName);
        oMethod.append("();");
        oMethod.appendNewLine();
        append(fieldName);
    }

    @Override
    public void visitAddExpr(@NotNull DexAddExpr o) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Result");
        String funcName = "add";
        oClass.referenced(o);
        String fieldName = oClass.addField("addResult", "Result");
        OutExpr oLeftExpr = new OutExpr(oMethod, o.getLeft());
        OutExpr oRightExpr = new OutExpr(oMethod, o.getRight());
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
    public void visitReferenceExpression(@NotNull DexReferenceExpression oRefExpr) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Object");
        append(oRefExpr);
    }

    private void genGetResult(DexExpression iExpr) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Object");
        append("(");
        append(new OutExpr(oMethod, iExpr));
        append(".result1__())");
    }
}
