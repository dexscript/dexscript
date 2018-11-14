package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class TransFunc extends DexVisitor {

    private final OutMethod oMethod;
    private final DexFunctionDeclaration decl;

    public TransFunc(OutMethod oMethod, DexFunctionDeclaration decl) {
        this.oMethod = oMethod;
        this.decl = decl;
    }

    @Override
    public void visitSimpleStatement(@NotNull DexSimpleStatement o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitShortVarDeclaration(@NotNull DexShortVarDeclaration o) {
        List<DexVarDefinition> varDefs = o.getVarDefinitionList();
        OutValue[] outVals = new OutValue[varDefs.size()];
        for (int i = 0; i < outVals.length; i++) {
            outVals[i] = new OutValue(oMethod.iFile());
        }
        DexExpression expr = o.getExpressionList().get(0);
        expr.accept(new TransExpr(oMethod.oClass(), outVals));
        for (int i = 0; i < varDefs.size(); i++) {
            DexVarDefinition inVar = varDefs.get(i);
            String inVarName = inVar.getIdentifier().getNode().getText();
            OutValue outVal = outVals[i];
            String fieldType = "Object";
            if (outVal.type != null) {
                fieldType = outVal.type.className;
            }
            String outFieldName = oMethod.oClass().addField(inVarName, fieldType);
            oMethod.appendSourceLine(inVar);
            oMethod.append(outFieldName);
            oMethod.append(" = ");
            oMethod.append(outVal.toString());
            oMethod.append(';');
            oMethod.appendNewLine();
        }
    }

    @Override
    public void visitReturnStatement(@NotNull DexReturnStatement o) {
        super.visitReturnStatement(o);
        oMethod.appendSourceLine(o);
        DexExpression expr = o.getExpressionList().get(0);
        OutValue val1 = new OutValue(oMethod.iFile());
        val1.type = TransType.translateType(decl.getSignature().getResult().getType());
        expr.accept(new TransExpr(oMethod.oClass(), val1));
        oMethod.append("result1__ = ");
        oMethod.append(val1.toString());
        oMethod.append(';');
        oMethod.appendNewLine();
        oMethod.append("finish();");
    }
}
