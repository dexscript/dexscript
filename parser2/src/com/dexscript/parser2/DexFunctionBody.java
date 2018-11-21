package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;

public class DexFunctionBody implements DexElement {

    private final Text matched;
    private DexSignature signature;
    private DexBlock block;

    public DexFunctionBody(Text src) {
        DexFunction nextFunction = new DexFunction(src);
        if (nextFunction.matched()) {
            matched = new Text(src.bytes, src.begin, nextFunction.begin());
        } else {
            matched = src;
        }
    }

    @Override
    public Text src() {
        return matched;
    }

    @Override
    public int begin() {
        return matched.begin;
    }

    public int end() {
        return matched.end;
    }

    @Override
    public boolean matched() {
        return true;
    }

    @Override
    public DexError err() {
        return null;
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    public DexSignature signature() {
        if (signature == null) {
            signature = new DexSignature(matched);
        }
        return signature;
    }

    public DexBlock block() {
        if (block == null) {
            block = new DexBlock(new Text(matched.bytes, signature().end(), matched.end));
        }
        return block;
    }
}
