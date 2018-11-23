package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;

public interface DexRootDecl extends DexElement {

    static DexRootDecl parse(Text src) {
        return new DexFunction(src);
    }
}
