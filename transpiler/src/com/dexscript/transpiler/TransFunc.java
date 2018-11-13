package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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
