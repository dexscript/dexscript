package com.dexscript.ast.type;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexType extends DexElement {

    public DexType(Text src) {
        super(src);
    }

    public abstract int leftRank();

    public static DexType parse(Text src) {
        DexType type = new DexStringLiteralType(src);
        if (type.matched()) {
            return type;
        }
        type = new DexVoidType(src);
        if (type.matched()) {
            return type;
        }
        return new DexTypeRef(src);
    }

    public void reparent(DexElement parent) {
        this.parent = parent;
    }
}
