package com.dexscript.transpiler2.gen;

import com.dexscript.parser2.core.DexElement;

public final class Gen {

    private final StringBuilder gen = new StringBuilder();
    private String prefix = "";

    public Gen __(String str) {
        gen.append(str);
        return this;
    }

    public Gen __(char c) {
        gen.append(c);
        return this;
    }

    public Gen __(DexElement elem) {
        gen.append(elem.toString());
        return this;
    }

    public Gen __(NL ignored) {
        gen.append(System.lineSeparator());
        gen.append(prefix);
        return this;
    }

    public Gen __(Indent indent) {
        String oldPrefix = prefix;
        prefix += "  ";
        __(new NL());
        indent.op.apply();
        prefix = oldPrefix;
        __(new NL());
        return this;
    }

    @Override
    public String toString() {
        return gen.toString();
    }
}
