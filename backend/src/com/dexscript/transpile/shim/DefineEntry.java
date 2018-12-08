package com.dexscript.transpile.shim;

import com.dexscript.dispatch.ImplEntry;
import com.dexscript.dispatch.VirtualEntry;
import com.dexscript.transpile.gen.*;

import java.util.List;

interface DefineEntry {
    static void $(Gen g, VirtualEntry virtualEntry, List<ImplEntry> concreteEntries) {
        g.__("public static Object "
        ).__(virtualEntry.funcName());
        DeclareParams.$(g, virtualEntry.paramsCount(), false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__(new Line("Scheduler scheduler = new Scheduler();"));
            for (ImplEntry implEntry : concreteEntries) {
                g.__("if ("
                ).__(implEntry.canF());
                InvokeParams.$(g, virtualEntry.paramsCount(), false);
                g.__(") {");
                g.__(new Indent(() -> {
                    g.__("Promise result = "
                    ).__(implEntry.newF());
                    InvokeParams.$(g, virtualEntry.paramsCount(), true);
                    g.__(new Line(";"));
                    g.__(new Line("scheduler.schedule();"));
                    g.__(new Line("return result.value();"));
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new DexRuntimeException();"));
        }));
        g.__(new Line("}"));
    }
}
