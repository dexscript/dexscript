package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;

public class DexFunctionBody {

    private final Text matched;

    public DexFunctionBody(Text src) {
        DexFunction nextFunction = new DexFunction(src);
        if (nextFunction.matched()) {
            matched = new Text(src.bytes, src.begin, nextFunction.begin());
        } else {
            matched = src;
        }
    }

    public int end() {
        return matched.end;
    }
}
