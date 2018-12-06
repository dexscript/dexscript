package com.dexscript.transpile.shim;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.type.TranslateType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.List;

interface DefineCan {

    static void $(Gen g, TypeSystem ts, DexFunction function, String canF) {
        List<String> typeChecks = new ArrayList<>();
        for (DexParam param : function.params()) {
            Type type = InferType.$(ts, param.paramType());
            String typeCheck = TranslateType.$(g, type, null, null);
            typeChecks.add(typeCheck);
        }
        g.__("public static boolean "
        ).__(canF);
        DeclareParams.$(g, function.params().size());
        g.__(" {");
        g.__(new Indent(() -> {
            for (int i = 0; i < typeChecks.size(); i++) {
                String typeCheck = typeChecks.get(i);
                g.__("if (!"
                ).__(typeCheck
                ).__("(arg"
                ).__(i
                ).__(new Line(")) { return false; }"));
            }
            g.__("return true;");
        }));
        g.__(new Line("}"));
    }
}
