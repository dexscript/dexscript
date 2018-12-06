package com.dexscript.transpile.shim;

import com.dexscript.transpile.gen.*;
import com.dexscript.type.FunctionType;

import java.util.List;

interface CombineNew {

    static void $(Gen g, List<FunctionType> funcTypes, int paramsCount, String cNewF) {
        g.__("public static Result "
        ).__(cNewF);
        DeclareParams.$(g, paramsCount, true);
        g.__(" {");
        g.__(new Indent(() -> {
            for (FunctionType funcType : funcTypes) {
                ConcreteEntry concreteEntry = funcType.definedBy().attachmentOfType(ConcreteEntry.class);
                g.__("if ("
                ).__(concreteEntry.canF());
                InvokeParams.$(g, paramsCount, true);
                g.__(new Line(") {"));
                g.__(new Indent(() -> {
                    g.__("return "
                    ).__(concreteEntry.newF());
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
