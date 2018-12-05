package com.dexscript.transpile.gen;

public interface DeclareParams {

    static void $(Gen g, int paramsCount) {
        g.__('(');
        for (int i = 0; i < paramsCount; i++) {
            if (i > 0) {
                g.__(", ");
            }
            g.__("Object arg"
            ).__(i);
        }
        g.__(')');
    }
}
