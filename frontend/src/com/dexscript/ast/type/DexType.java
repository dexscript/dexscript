package com.dexscript.ast.type;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexType extends DexElement {

    public DexType(Text src) {
        super(src);
    }

    public abstract int leftRank();

    public static DexType parse(Text src) {
        DexType type = new DexTypeRef(src);
        if (type.matched()) {
            return type;
        }
        return new DexStringLiteralType(src);
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }
}
