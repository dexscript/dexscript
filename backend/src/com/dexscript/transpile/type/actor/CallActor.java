package com.dexscript.transpile.type.actor;

import com.dexscript.ast.DexActor;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.type.FunctionType;

class CallActor extends FunctionImpl {

    private final DexActor actor;
    private Boolean hasAwait;

    public CallActor(OutShim oShim, FunctionType functionType, DexActor actor) {
        super(oShim, functionType);
        this.actor = actor;
    }


    @Override
    public boolean hasAwait() {
        if (hasAwait == null) {
            hasAwait = new HasAwait(oShim.typeSystem(), actor).result();
        }
        return hasAwait;
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String newF = oShim.allocateShim("call__" + actor.actorName());
        g.__("public static Promise "
        ).__(newF);
        DeclareParams.$(g, functionType.params().size(), true);
        g.__(" {");
        g.__(new Indent(() -> {
            String className = ActorType.qualifiedClassNameOf(actor);
            g.__("return new "
            ).__(className
            ).__("(scheduler");
            for (int i = 0; i < functionType.params().size(); i++) {
                g.__(", ");
                g.__("arg"
                ).__(i);
            }
            g.__(");");
        }));
        g.__(new Line("}"));
        return newF;
    }
}