package com.dexscript.transpile.shim;

import com.dexscript.dispatch.ImplEntry;
import com.dexscript.transpile.gen.*;
import com.dexscript.type.FunctionType;

import java.util.List;

interface CombineNew {

    static void $(Gen g, List<FunctionType> funcTypes, int paramsCount, String cNewF) {
        g.__("public static Promise "
        ).__(cNewF);
        DeclareParams.$(g, paramsCount, true);
        g.__(" {");
        g.__(new Indent(() -> {
            for (FunctionType funcType : funcTypes) {
                ImplEntry implEntry = funcType.definedBy().attachmentOfType(ImplEntry.class);
                g.__("if ("
                ).__(implEntry.canF());
                InvokeParams.$(g, paramsCount, true);
                g.__(new Line(") {"));
                g.__(new Indent(() -> {
                    g.__("return "
                    ).__(implEntry.newF());
                    InvokeParams.$(g, paramsCount, true);
                    g.__(new Line(";"));
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new RuntimeException();"));
        }));
        g.__(new Line("}"));
    }
}
