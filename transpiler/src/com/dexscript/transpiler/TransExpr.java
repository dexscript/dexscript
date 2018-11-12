package com.dexscript.transpiler;

import com.dexscript.psi.DexStringLiteral;
import com.dexscript.psi.DexVisitor;
import org.jetbrains.annotations.NotNull;

public class TransExpr extends DexVisitor {

    private final TransOutput out;

    public TransExpr(TransOutput out) {
        this.out = out;
    }

    @Override
    public void visitStringLiteral(@NotNull DexStringLiteral o) {
        String text = o.getNode().getText();
        out.append('"');
        out.append(text.substring(1, text.length() - 1));
        out.append('"');
    }
}
