package com.dexscript.ast.inf;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexInfMember extends DexElement {

    protected DexInfMember prev;

    public DexInfMember(Text src) {
        super(src);
    }

    public static DexInfMember parse(Text src) {
        DexInfMember member = new DexInfFunction(src);
        if (member.matched()) {
            return member;
        }
        return new DexInfMethod(src);
    }

    public final void reparent(DexElement parent, DexInfMember prev) {
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
