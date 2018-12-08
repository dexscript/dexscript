package com.dexscript.transpile.shim;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.dispatch.ActorEntry;
import com.dexscript.dispatch.InnerActorEntry;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

interface DefineNew {

    static void $(Gen g, TypeSystem ts, ActorEntry actor) {
        DexFunction function = actor.function();
        String newF = OutShim.stripPrefix(actor.newF());
        g.__("public static Promise "
        ).__(newF);
        DeclareParams.$(g, function.params().size(), true);
        g.__(" {");
        g.__(new Indent(() -> {
            String className = OutTopLevelClass.qualifiedClassNameOf(function);
            g.__("return new "
            ).__(className
            ).__("(scheduler");
            for (int i = 0; i < function.params().size(); i++) {
                g.__(", ");
                DexParam param = function.params().get(i);
                Type paramType = ts.resolveType(param.paramType());
                g.__("(("
                ).__(paramType.javaClassName()
                ).__(")arg"
                ).__(i
                ).__(')');
            }
            g.__(");");
        }));
        g.__(new Line("}"));
    }

    static void $(Gen g, TypeSystem ts, InnerActorEntry innerActor) {
        String newF = OutShim.stripPrefix(innerActor.newF());
        DexAwaitConsumer awaitConsumer = innerActor.awaitConsumer();
        String outerClassName = innerActor.outerClassName();
        g.__("public static Promise "
        ).__(newF);
        DeclareParams.$(g, awaitConsumer.params().size() + 1, true);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__(outerClassName
            ).__(" obj = ("
            ).__(outerClassName
            ).__(new Line(")arg0;"));
            g.__("return obj.new "
            ).__(awaitConsumer.identifier().toString()
            ).__("(scheduler");
            for (int i = 1; i < awaitConsumer.params().size() + 1; i++) {
                g.__(", ");
                DexParam param = awaitConsumer.params().get(i);
                Type paramType = ts.resolveType(param.paramType());
                g.__("(("
                ).__(paramType.javaClassName()
                ).__(")arg"
                ).__(i
                ).__(')');
            }
            g.__(");");
        }));
        g.__(new Line("}"));
    }
}
