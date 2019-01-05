package com.dexscript.shim.actor;

import com.dexscript.ast.DexActor;
import com.dexscript.gen.DeclareParams;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.OutShim;
import com.dexscript.shim.java.FunctionImpl;
import com.dexscript.type.core.FunctionType;

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
        int paramsCount = functionType.params().size() + 1; // context param
        DeclareParams.$(g, paramsCount, true);
        g.__(" {");
        g.__(new Indent(() -> {
            String className = ActorType.qualifiedClassNameOf(actor);
            g.__("return new "
            ).__(className
            ).__("(scheduler");
            for (int i = 0; i < paramsCount; i++) {
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
