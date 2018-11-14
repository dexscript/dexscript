package com.dexscript.transpiler;

import com.dexscript.psi.DexFile;
import com.dexscript.psi.DexStringLiteral;
import org.jetbrains.annotations.NotNull;

public class OutExpr extends OutValue {

    OutExpr(DexFile iFile) {
        super(iFile);
    }

    @Override
    public void visitStringLiteral(@NotNull DexStringLiteral o) {
        String text = o.getNode().getText();
        append('"');
        append(text.substring(1, text.length() - 1));
        append('"');
    }
}
