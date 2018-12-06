package com.dexscript.transpile.shim;

import com.dexscript.transpile.gen.*;

import java.util.List;

interface DefineEntry {
    static void $(Gen g, VirtualEntry virtualEntry, List<ConcreteEntry> concreteEntries) {
        g.__("public static Object "
        ).__(virtualEntry.funcName());
        DeclareParams.$(g, virtualEntry.paramsCount());
        g.__(" {");
        g.__(new Indent(() -> {
            for (ConcreteEntry concreteEntry : concreteEntries) {
                g.__("if ("
                ).__(concreteEntry.canF());
                InvokeParams.$(g, virtualEntry.paramsCount());
                g.__(new Line(") {"));
                g.__(new Indent(() -> {
                    g.__("return "
                    ).__(concreteEntry.newF());
                    InvokeParams.$(g, virtualEntry.paramsCount());
                    g.__(new Line(".value();"));
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new RuntimeException();"));
        }));
        g.__(new Line("}"));
    }
}
