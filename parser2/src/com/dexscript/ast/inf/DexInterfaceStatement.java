package com.dexscript.ast.inf;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexInterfaceStatement extends DexElement {

    protected DexInterfaceStatement prev;

    public DexInterfaceStatement(Text src) {
        super(src);
    }

    public static DexInterfaceStatement parse(Text src) {
        return new DexInfMethod(src);
    }

    public final void reparent(DexElement parent, DexInterfaceStatement prev) {
        this.parent = parent;
        this.prev = prev;
    }

    @Override
    public final DexElement prev() {
        if (prev != null) {
            return prev;
        } else {
            return parent();
        }
    }
}
