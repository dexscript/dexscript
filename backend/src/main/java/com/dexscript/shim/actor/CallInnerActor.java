package com.dexscript.shim.actor;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.gen.DeclareParams;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.OutShim;
import com.dexscript.shim.java.FunctionImpl;
import com.dexscript.type.core.FunctionType;

class CallInnerActor extends FunctionImpl {

    private final DexAwaitConsumer awaitConsumer;
    private final String outerClassName;
    private Boolean hasAwait;

    public CallInnerActor(OutShim oShim, FunctionType functionType, String outerClassName,
                          DexAwaitConsumer awaitConsumer) {
        super(oShim, functionType);
        this.outerClassName = outerClassName;
        this.awaitConsumer = awaitConsumer;
    }

    @Override
    public boolean hasAwait() {
        if (hasAwait == null) {
            hasAwait = new HasAwait(oShim.typeSystem(), awaitConsumer).result();
        }
        return hasAwait;
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String newF = oShim.allocateShim("call__" + functionType.name());
        g.__("public static Promise "
        ).__(newF);
        DeclareParams.$(g, awaitConsumer.params().size() + 2, true);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__(outerClassName
            ).__(" obj = ("
            ).__(outerClassName
            ).__(new Line(")arg0;"));
            g.__("return obj.new "
            ).__(awaitConsumer.identifier().toString()
            ).__("(scheduler");
            for (int i = 1; i < functionType.params().size() + 1; i++) {
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
