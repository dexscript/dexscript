package com.dexscript.transpile.shim;

import com.dexscript.transpile.gen.*;

import java.util.List;

interface DefineEntry {
    static void $(Gen g, VirtualEntry virtualEntry, List<ConcreteEntry> concreteEntries) {
        g.__("public static Object "
        ).__(virtualEntry.funcName());
        DeclareParams.$(g, virtualEntry.paramsCount(), false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__(new Line("Scheduler scheduler = new Scheduler();"));
            for (ConcreteEntry concreteEntry : concreteEntries) {
                g.__("if ("
                ).__(concreteEntry.canF());
                InvokeParams.$(g, virtualEntry.paramsCount(), false);
                g.__(") {");
                g.__(new Indent(() -> {
                    g.__("Result result = "
                    ).__(concreteEntry.newF());
                    InvokeParams.$(g, virtualEntry.paramsCount(), true);
                    g.__(new Line(";"));
                    g.__(new Line("scheduler.schedule();"));
                    g.__(new Line("return result.value();"));
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new RuntimeException();"));
        }));
        g.__(new Line("}"));
    }
}
