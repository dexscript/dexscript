package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TransFunc extends DexVisitor {

    private final TranspiledClass out;
    private final DexFunctionDeclaration decl;

    public TransFunc(TranspiledClass out, DexFunctionDeclaration decl) {
        this.out = out;
        this.decl = decl;
    }

    @Override
    public void visitSimpleStatement(@NotNull DexSimpleStatement o) {
        o.acceptChildren(this);
    }

    @Override
    public void visitShortVarDeclaration(@NotNull DexShortVarDeclaration o) {
        List<DexVarDefinition> varDefs = o.getVarDefinitionList();
        TranspiledValue[] outVals = new TranspiledValue[varDefs.size()];
        for (int i = 0; i < outVals.length; i++) {
            outVals[i] = new TranspiledValue();
        }
        DexExpression expr = o.getExpressionList().get(0);
        expr.accept(new TransExpr(out, outVals));
        for (int i = 0; i < varDefs.size(); i++) {
            DexVarDefinition inVar = varDefs.get(i);
            String inVarName = inVar.getIdentifier().getNode().getText();
            TranspiledValue outVal = outVals[i];
            String fieldType = "Object";
            if (outVal.type != null) {
                fieldType = outVal.type.className;
            }
            String outFieldName = out.addField(inVarName, fieldType);
            out.appendSourceLine(inVar);
            out.append(outFieldName);
            out.append(" = ");
            out.append(outVal.out.toString());
            out.append(';');
            out.appendNewLine();
        }
    }

    @Override
    public void visitReturnStatement(@NotNull DexReturnStatement o) {
        super.visitReturnStatement(o);
        out.appendSourceLine(o);
        DexExpression expr = o.getExpressionList().get(0);
        TranspiledValue val1 = new TranspiledValue();
        val1.type = TransType.translateType(decl.getSignature().getResult().getType());
        expr.accept(new TransExpr(out, val1));
        out.append("result1__ = ");
        out.append(val1.out.toString());
        out.append(';');
        out.appendNewLine();
        out.append("finish();");
    }
}
