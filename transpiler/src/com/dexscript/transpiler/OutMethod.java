package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.dexscript.psi.impl.DexPsiImplUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OutMethod extends OutCode {

    private OutClass oClass;
    private final DexSignature iSig;

    public OutMethod(OutClass oClass, DexSignature iSig) {
        super(oClass);
        this.oClass = oClass;
        this.iSig = iSig;
        oClass.addMethod(this);
    }

    public OutClass oClass() {
        return oClass;
    }


    @Override
    public void visitReturnStatement(@NotNull DexReturnStatement o) {
        super.visitReturnStatement(o);
        appendSourceLine(o);
        if (o.getExpressionList().size() == 1) {
            DexExpression iExpr = o.getExpressionList().get(0);
            OutExpr oExpr = new OutExpr(this, iExpr);
            append("result1__ = ");
            append("(");
            append(TransType.translateType(iSig.getResult().getType()).className);
            append(")");
            if ("Result".equals(oExpr.type.className)) {
                append("((Result1)");
                append(oExpr.toString());
                append(").result1__()");
            } else {
                append(oExpr.toString());
            }
            append(';');
            appendNewLine();
        }
        append("finish();");
    }

    @Override
    public void visitSimpleStatement(@NotNull DexSimpleStatement o) {
        if (o.getShortVarDeclaration() != null) {
            o.acceptChildren(this);
            return;
        }
        if (o.getLeftHandExprList() != null) {
            for (DexExpression iExpr : o.getLeftHandExprList().getExpressionList()) {
                new OutExpr(this, iExpr);
            }
            return;
        }
    }

    @Override
    public void visitVarDeclaration(@NotNull DexVarDeclaration iVarDecl) {
        for (DexVarSpec iVarSpec : iVarDecl.getVarSpecList()) {
            for (DexVarDefinition iVarDef : iVarSpec.getVarDefinitionList()) {
                String varName = iVarDef.getIdentifier().getNode().getText();
                String varType = TransType.translateType(iVarSpec.getType()).className;
                oClass.addField(varName, varType);
            }
        }
    }

    @Override
    public void visitAssignmentStatement(@NotNull DexAssignmentStatement iAssignStmt) {
        List<DexExpression> iLeftExprs = iAssignStmt.getLeftHandExprList().getExpressionList();
        List<DexExpression> iRightExprs = iAssignStmt.getExpressionList();
        for (int i = 0; i < iLeftExprs.size(); i++) {
            DexExpression iLeftExpr = iLeftExprs.get(i);
            DexExpression iRightExpr = iRightExprs.get(i);
            OutExpr oLeftExpr = new OutExpr(this, iLeftExpr);
            OutExpr oRightExpr = new OutExpr(this, iRightExpr);
            append(oLeftExpr);
            append(iAssignStmt.getAssignOp());
            append(oRightExpr);
            appendNewLine(';');
        }
    }

    @Override
    public void visitStatement(@NotNull DexStatement iStmt) {
        iStmt.acceptChildren(this);
    }

    @Override
    public void visitShortVarDeclaration(@NotNull DexShortVarDeclaration iShortVarDecl) {
        DexExpression iExpr = iShortVarDecl.getExpressionList().get(0);
        OutExpr oExpr = new OutExpr(this, iExpr);
        List<DexVarDefinition> iVarDefs = iShortVarDecl.getVarDefinitionList();
        DexVarDefinition iVarDef = iVarDefs.get(0);
        String inVarName = iVarDef.getIdentifier().getNode().getText();
        String oFieldName = oClass.addField(inVarName, oExpr.type.className);
        appendSourceLine(iVarDef);
        append(oFieldName);
        append(" = ");
        append(oExpr);
        append(';');
        appendNewLine();
    }

    @Override
    public void visitAwaitStatement(@NotNull DexAwaitStatement iAwaitStmt) {
        DexServeStatement iServeStmt = iAwaitStmt.getServeStatement();
        OutMethod oMethod = new OutMethod(oClass, iSig);
        oMethod.append("public Result ");
        oMethod.append(iServeStmt.getIdentifier());
        oMethod.append('(');
        int paramsCount = DexPsiImplUtil.getParamsCount(iServeStmt.getSignature());
        oMethod.appendParamsDeclaration(paramsCount);
        oMethod.append(") {");
        oMethod.indent(() -> {
            OutInnerClass oInnerClass = new OutInnerClass(iServeStmt, oMethod);
            oMethod.append("return new ");
            oMethod.append(oInnerClass.className());
            oMethod.append('(');
            oMethod.appendParamsInvocation(paramsCount);
            oMethod.append(");");
            oClass.addServeBoat("", iServeStmt);
        });
        oMethod.appendNewLine("}");
    }
}
