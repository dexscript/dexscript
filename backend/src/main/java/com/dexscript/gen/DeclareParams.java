package com.dexscript.gen;

import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;

public interface DeclareParams {

    static void $(Gen g, int paramsCount, boolean addSchedulerParam) {
        g.__('(');
        if (addSchedulerParam) {
            g.__("Scheduler scheduler");
            if (paramsCount > 0) {
                g.__(", ");
            }
        }
        for (int i = 0; i < paramsCount; i++) {
            if (i > 0) {
                g.__(", ");
            }
            g.__("Object arg"
            ).__(i);
        }
        g.__(") throws Exception ");
    }

    static void $(Gen g, DexSig sig) {
        g.__("(Scheduler scheduler");
        for (int i = 0; i < sig.params().size(); i++) {
            g.__(", ");
            DexParam param = sig.params().get(i);
            g.__("Object "
            ).__(param.paramName());
        }
        g.__(", Object context) throws Exception ");
    }
}
