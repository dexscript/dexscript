package com.dexscript.transpile.shim;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.type.CheckType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.List;

interface DefineCan {

    static void $(Gen g, TypeSystem ts, ActorEntry actor) {
        DexFunction function = actor.function();
        String canF = actor.canF();
        List<String> typeChecks = new ArrayList<>();
        for (DexParam param : function.params()) {
            Type type = InferType.$(ts, param.paramType());
            String typeCheck = CheckType.$(g, type, null, null);
            typeChecks.add(typeCheck);
        }
        g.__("public static boolean "
        ).__(canF);
        DeclareParams.$(g, function.params().size(), false);
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

    static void $(Gen g, TypeSystem ts, InnerActorEntry innerActor) {
        DexAwaitConsumer awaitConsumer = innerActor.awaitConsumer();
        String outerClassName = innerActor.outerClassName();
        String canF = innerActor.canF();
        List<String> typeChecks = new ArrayList<>();
        for (DexParam param : awaitConsumer.params()) {
            Type type = InferType.$(ts, param.paramType());
            String typeCheck = CheckType.$(g, type, null, null);
            typeChecks.add(typeCheck);
        }
        g.__("public static boolean "
        ).__(canF);
        DeclareParams.$(g, awaitConsumer.params().size() + 1, false);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__("if (!(arg0 instanceof "
            ).__(outerClassName
            ).__(new Line(")) { return false; }"));
            for (int i = 1; i < typeChecks.size() + 1; i++) {
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
