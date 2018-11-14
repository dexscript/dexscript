package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class TransFunc extends DexVisitor {

    private OutMethod oMethod;
    private final DexSignature iSig;

    public TransFunc(OutMethod oMethod, DexSignature iSig) {
        this.oMethod = oMethod;
        this.iSig = iSig;
    }

    @Override
    public void visitSimpleStatement(@NotNull DexSimpleStatement o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitShortVarDeclaration(@NotNull DexShortVarDeclaration iShortVarDecl) {
        DexExpression iExpr = iShortVarDecl.getExpressionList().get(0);
        OutExpr oExpr = new OutExpr(oMethod, iExpr);
        List<DexVarDefinition> iVarDefs = iShortVarDecl.getVarDefinitionList();
        DexVarDefinition iVarDef = iVarDefs.get(0);
        String inVarName = iVarDef.getIdentifier().getNode().getText();
        String oFieldName = oMethod.oClass().addField(inVarName, oExpr.type.className);
        oMethod.appendSourceLine(iVarDef);
        oMethod.append(oFieldName);
        oMethod.append(" = ");
        oMethod.append(oExpr);
        oMethod.append(';');
        oMethod.appendNewLine();
    }

    @Override
    public void visitReturnStatement(@NotNull DexReturnStatement o) {
        super.visitReturnStatement(o);
        oMethod.appendSourceLine(o);
        DexExpression iExpr = o.getExpressionList().get(0);
        OutExpr val = new OutExpr(oMethod, iExpr);
        oMethod.append("result1__ = ");
        if ("Result".equals(val.type.className)) {
            oMethod.append("((");
            oMethod.append(TransType.translateType(iSig.getResult().getType()).className);
            oMethod.append(")");
            oMethod.append(val.toString());
            oMethod.append(".result1__())");
        } else {
            oMethod.append("(");
            oMethod.append(TransType.translateType(iSig.getResult().getType()).className);
            oMethod.append(")");
            oMethod.append(val.toString());
        }
        oMethod.append(';');
        oMethod.appendNewLine();
        oMethod.append("finish();");
    }


    @Override
    public void visitAwaitStatement(@NotNull DexAwaitStatement iAwaitStmt) {
        DexServeStatement iServeStmt = iAwaitStmt.getServeStatement();
        oMethod = new OutMethod(oMethod.oClass());
        oMethod.append("public Result ");
        oMethod.append(iServeStmt.getIdentifier());
        oMethod.append("() {");
        oMethod.indent(() -> {
            oMethod.append("return new Actor() {");
            oMethod.indent(() -> {
                OutClass oClass = new OutClass(oMethod);
                genReturnValueFields(oClass, iServeStmt.getSignature());
                OutMethod oSubMethod = new OutMethod(oClass);
                oSubMethod.append("{");
                oSubMethod.indent(() -> {
                    iServeStmt.getBlock().acceptChildren(new TransFunc(oSubMethod, iServeStmt.getSignature()));
                });
                oSubMethod.appendNewLine('}');
                oClass.genClassBody();
                oMethod.append(oClass.toString());
            });
            oMethod.append('}');
        });
        oMethod.appendNewLine("}");
    }

    public static void genReturnValueFields(OutClass oClass, @NotNull DexSignature iSig) {
        DexResult result = iSig.getResult();
        if (result == null) {
            return;
        }
        DexType returnType = result.getType();
        oClass.append("public ");
        oClass.append(returnType);
        oClass.append(" result1__;");
        oClass.appendNewLine();
        oClass.append("public Object result1__() {");
        oClass.indent(() -> {
            oClass.append("return result1__;");
        });
        oClass.append("}");
        oClass.appendNewLine();
    }
}
