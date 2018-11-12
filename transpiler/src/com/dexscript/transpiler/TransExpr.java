package com.dexscript.transpiler;

import com.dexscript.psi.DexCallExpr;
import com.dexscript.psi.DexStringLiteral;
import com.dexscript.psi.DexVisitor;
import org.jetbrains.annotations.NotNull;

class TransExpr extends DexVisitor {

    private final TempVariableContext tempVarCtx;
    private final TranspiledClass out;

    public TransExpr(TempVariableContext tempVarCtx, TranspiledClass out) {
        this.tempVarCtx = tempVarCtx;
        this.out = out;
    }

    @Override
    public void visitStringLiteral(@NotNull DexStringLiteral o) {
        out.append("Object ");
        String varName = tempVarCtx.assignVariableName();
        out.append(varName);
        out.append(" = ");
        String text = o.getNode().getText();
        out.append('"');
        out.append(text.substring(1, text.length() - 1));
        out.append('"');
        out.append(';');
        out.appendNewLine();
    }

    @Override
    public void visitCallExpr(@NotNull DexCallExpr o) {
        out.append("Object ");
        String varName = tempVarCtx.assignVariableName();
        out.append(varName);
        out.append(" = ");
        out.append(o.getExpression());
        out.append("__ctor.create();");
        out.referenced(o);
        out.appendNewLine();
        out.append("Object ");
        out.append(tempVarCtx.assignVariableName());
        out.append(" = ((Result1Inf)");
        out.append(varName);
        out.append(").result1__();");
        out.appendNewLine();
    }
}
