package com.dexscript.shim.gen;

import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.gen.Gen;

public interface InvokeParams {

    static void $(Gen g, int paramsCount, boolean addSchedulerArg) {
        g.__('(');
        if (addSchedulerArg) {
            g.__("scheduler");
            if (paramsCount > 0) {
                g.__(", ");
            }
        }
        for (int i = 0; i < paramsCount; i++) {
            if (i > 0) {
                g.__(", ");
            }
            g.__("arg"
            ).__(i);
        }
        g.__(')');
    }

    static void $(Gen g, DexSig sig) {
        g.__('(');
        g.__("scheduler");
        for (int i = 0; i < sig.params().size(); i++) {
            g.__(", ");
            DexParam param = sig.params().get(i);
            g.__(param.paramName());
        }
        g.__(')');
    }
}
