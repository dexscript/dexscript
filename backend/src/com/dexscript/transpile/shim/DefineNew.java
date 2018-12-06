package com.dexscript.transpile.shim;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.OutTopLevelClass;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

interface DefineNew {

    static void $(Gen g, TypeSystem ts, DexFunction function, String newF) {
        g.__("public static Result "
        ).__(newF
        ).__('(');
        for (int i = 0; i < function.params().size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            DexParam param = function.params().get(i);
            g.__("Object "
            ).__(param.paramName());
        }
        g.__(") {");
        g.__(new Indent(() -> {
            String className = OutTopLevelClass.qualifiedClassNameOf(function);
            g.__("return new "
            ).__(className
            ).__('(');
            for (int i = 0; i < function.params().size(); i++) {
                if (i > 0) {
                    g.__(", ");
                }
                DexParam param = function.params().get(i);
                Type paramType = InferType.$(ts, param.paramType());
                g.__("(("
                ).__(paramType.javaClassName()
                ).__(')'
                ).__(param.paramName()
                ).__(')');
            }
            g.__(");");
        }));
        g.__(new Line("}"));
    }
}
