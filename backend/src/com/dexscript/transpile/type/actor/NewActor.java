package com.dexscript.transpile.type.actor;

import com.dexscript.ast.DexActor;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

class NewActor extends FunctionImpl {

    private final DexActor actor;

    public NewActor(OutShim oShim, FunctionType functionType, DexActor actor) {
        super(oShim, functionType);
        this.actor = actor;
    }

    @Override
    public boolean hasAwait() {
        return true;
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String newF = oShim.allocateShim("new__" + actor.actorName());
        g.__("public static Promise "
        ).__(newF);
        DeclareParams.$(g, functionType.params().size(), true);
        g.__(" {");
        g.__(new Indent(() -> {
            String className = ActorType.qualifiedClassNameOf(actor);
            g.__("return new "
            ).__(className
            ).__("(scheduler");
            for (int i = 1; i < functionType.params().size(); i++) {
                g.__(", ");
                Type paramType = functionType.params().get(i);
                g.__("(("
                ).__(paramType.javaClassName()
                ).__(")arg"
                ).__(i
                ).__(')');
            }
            g.__(");");
        }));
        g.__(new Line("}"));
        return newF;
    }
}
