package com.dexscript.parser2;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.Text;

import java.util.Arrays;
import java.util.List;

public interface DexRootDecl extends DexElement {

    static DexRootDecl parse(Text src) {
        return new DexFunction(src);
    }
}
