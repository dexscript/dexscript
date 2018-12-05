package com.dexscript.transpile.gen;

public interface InvokeParams {

    static void $(Gen g, int paramsCount) {
        g.__('(');
        for (int i = 0; i < paramsCount; i++) {
            if (i > 0) {
                g.__(", ");
            }
            g.__("arg"
            ).__(i);
        }
        g.__(')');
    }
}
