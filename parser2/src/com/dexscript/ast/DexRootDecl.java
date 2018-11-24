package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public abstract class DexRootDecl extends DexElement {

    public DexRootDecl(Text src) {
        super(src);
    }

    public static DexRootDecl parse(Text src) {
        return new DexFunction(src);
    }

    public DexFunction asFunction() {
        return (DexFunction) this;
    }
}
