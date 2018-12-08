package com.dexscript.transpile.gen;

import com.dexscript.ast.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

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
        g.__(')');
    }

    static void $(Gen g, TypeSystem ts, DexSig sig) {
        g.__("(Scheduler scheduler");
        for (int i = 0; i < sig.params().size(); i++) {
            g.__(", ");
            DexParam param = sig.params().get(i);
            Type type = ts.resolveType(param.paramType());
            g.__(type.javaClassName());
            g.__(' '
            ).__(param.paramName());
        }
        g.__(')');
    }
}
