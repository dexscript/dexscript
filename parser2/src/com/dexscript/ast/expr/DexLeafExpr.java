package com.dexscript.ast.expr;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.func.DexStatement;

public abstract class DexLeafExpr extends DexExpr {

    public DexLeafExpr(Text src) {
        super(src);
    }

    @Override
    public final void walkDown(Visitor visitor) {
    }

    @Override
    public final void reparent(DexElement parent, DexStatement stmt) {
        super.reparent(parent, stmt);
    }
}
