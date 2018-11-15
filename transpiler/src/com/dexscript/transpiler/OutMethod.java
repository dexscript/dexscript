package com.dexscript.transpiler;

import com.dexscript.psi.*;
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
        if (iSig.getResult() != null && o.getExpressionList().size() == 1) {
            DexExpression iExpr = o.getExpressionList().get(0);
            OutExpr val = new OutExpr(this, iExpr);
            append("result1__ = ");
            append("(");
            append(TransType.translateType(iSig.getResult().getType()).className);
            append(")");
            append(val.toString());
            if ("Result".equals(val.type.className)) {
                append(".result1__()");
            }
            append(';');
            appendNewLine();
        }
        append("finish();");
    }

    @Override
    public void visitSimpleStatement(@NotNull DexSimpleStatement o) {
        o.acceptChildren(this);
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
        oMethod.append("() {");
        oMethod.indent(() -> {
            oMethod.append("return new Actor() {");
            oMethod.indent(() -> {
                OutClass oClass = new OutClass(oMethod);
                oClass.appendReturnValueFields(iServeStmt.getSignature());
                OutMethod oSubMethod = new OutMethod(oClass, iSig);
                oSubMethod.append("{");
                oSubMethod.indent(() -> {
                    iServeStmt.getBlock().acceptChildren(oSubMethod);
                });
                oSubMethod.appendNewLine('}');
                oClass.genClassBody();
                oMethod.append(oClass.toString());
            });
            oMethod.append('}');
        });
        oMethod.appendNewLine("}");
    }
}
