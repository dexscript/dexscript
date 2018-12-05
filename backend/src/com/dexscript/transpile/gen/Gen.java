package com.dexscript.transpile.gen;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.elem.DexSig;

import java.util.List;

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

    public void indention(String indention) {
        this.indention = indention;
    }

    public Gen __(Object obj) {
        gen.append(obj.toString());
        return this;
    }

    public Gen __(String str) {
        gen.append(str);
        return this;
    }

    public Gen __(char c) {
        gen.append(c);
        return this;
    }

    public Gen __(DexSig sig) {
        __('(');
        List<DexParam> params = sig.params();
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                __(", ");
            }
            DexParam param = params.get(i);
            __(param.paramName());
        }
        __(')');
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
