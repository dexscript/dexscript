package com.dexscript.transpile.gen;

import com.dexscript.ast.core.DexElement;

public final class Gen {

    private final StringBuilder gen = new StringBuilder();
    private String prefix;

    public Gen() {
        this("");
    }

    public Gen(String prefix) {
        this.prefix = prefix;
    }

    public String prefix() {
        return prefix;
    }

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

    public Gen __(Line line) {
        gen.append(line.line);
        gen.append(System.lineSeparator());
        gen.append(prefix);
        return this;
    }

    public Gen __(Indent indent) {
        String oldPrefix = prefix;
        prefix += "  ";
        __(new Line());
        indent.op.apply();
        prefix = oldPrefix;
        __(new Line());
        return this;
    }

    @Override
    public String toString() {
        return gen.toString();
    }
}
