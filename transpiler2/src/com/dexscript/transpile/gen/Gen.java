package com.dexscript.transpile.gen;

import com.dexscript.ast.core.DexElement;

public final class Gen {

    private final StringBuilder gen = new StringBuilder();
    private String indention;

    public Gen() {
        this("");
    }

    public Gen(String indention) {
        this.indention = indention;
    }

    public String indention() {
        return indention;
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
        gen.append(indention);
        return this;
    }

    public Gen __(Indent indent) {
        String oldPrefix = indention;
        indention += "  ";
        __(new Line());
        indent.op.apply();
        indention = oldPrefix;
        __(new Line());
        return this;
    }

    @Override
    public String toString() {
        return gen.toString();
    }
}
