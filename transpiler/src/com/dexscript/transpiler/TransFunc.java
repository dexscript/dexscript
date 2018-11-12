package com.dexscript.transpiler;

import com.dexscript.psi.DexExpression;
import com.dexscript.psi.DexReturnStatement;
import com.dexscript.psi.DexVisitor;
import org.jetbrains.annotations.NotNull;

public class TransFunc extends DexVisitor {

    private final TransOutput out;

    public TransFunc(TransOutput out) {
        this.out = out;
    }

    @Override
    public void visitReturnStatement(@NotNull DexReturnStatement o) {
        super.visitReturnStatement(o);
        out.appendSourceLine(o);
        out.append("result1__ = ");
        DexExpression expr = o.getExpressionList().get(0);
        expr.accept(new TransExpr(out));
        out.append(';');
        out.appendNewLine();
        out.append("finish();");
    }
}
