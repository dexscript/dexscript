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
        List<TransExpr.ExpectedValue> expectedValues = new ArrayList<>();
        List<DexVarDefinition> varDefs = o.getVarDefinitionList();
        for (DexVarDefinition varDef : varDefs) {
            expectedValues.add(new TransExpr.ExpectedValue());
        }
        DexExpression expr = o.getExpressionList().get(0);
        expr.accept(new TransExpr(out, expectedValues));
        for (int i = 0; i < varDefs.size(); i++) {
            String inVarName = varDefs.get(i).getIdentifier().getNode().getText();
            String outFieldName = out.addField(inVarName, "Object");
            out.append(outFieldName);
            out.append(" = ");
            out.append(expectedValues.get(i).out.toString());
            out.append(';');
            out.appendNewLine();
        }
    }

    @Override
    public void visitReturnStatement(@NotNull DexReturnStatement o) {
        super.visitReturnStatement(o);
        out.appendSourceLine(o);
        DexExpression expr = o.getExpressionList().get(0);
        TransExpr.ExpectedValue val1 = new TransExpr.ExpectedValue();
        val1.type = TransType.translateType(decl.getSignature().getResult().getType());
        expr.accept(new TransExpr(out, Arrays.asList(val1)));
        out.append("result1__ = ");
        out.append(val1.out.toString());
        out.append(';');
        out.appendNewLine();
        out.append("finish();");
    }
}
