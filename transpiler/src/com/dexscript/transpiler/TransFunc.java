package com.dexscript.transpiler;

import com.dexscript.psi.DexExpression;
import com.dexscript.psi.DexFunctionDeclaration;
import com.dexscript.psi.DexReturnStatement;
import com.dexscript.psi.DexVisitor;
import org.jetbrains.annotations.NotNull;

class TransFunc extends DexVisitor {

    private final TranspiledClass out;
    private final DexFunctionDeclaration decl;

    public TransFunc(TranspiledClass out, DexFunctionDeclaration decl) {
        this.out = out;
        this.decl = decl;
    }

    @Override
    public void visitReturnStatement(@NotNull DexReturnStatement o) {
        super.visitReturnStatement(o);
        TempVariableContext tempVarCtx = new TempVariableContext();
        out.appendSourceLine(o);
        DexExpression expr = o.getExpressionList().get(0);
        expr.accept(new TransExpr(tempVarCtx, out));
        out.append("result1__ = (");
        out.append(decl.getSignature().getResult().getType());
        out.append(')');
        out.append(tempVarCtx.lastVariableName());
        out.append(';');
        out.appendNewLine();
        out.append("finish();");
    }
}
