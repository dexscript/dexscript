package com.dexscript.shim.actor;

import com.dexscript.ast.DexActor;
import com.dexscript.gen.DeclareParams;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.OutShim;
import com.dexscript.shim.java.FunctionImpl;
import com.dexscript.type.FunctionType;

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
                g.__("arg"
                ).__(i);
            }
            g.__(");");
        }));
        g.__(new Line("}"));
        return newF;
    }
}
