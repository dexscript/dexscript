package com.dexscript.transpile.type.actor;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.HasAwait;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.FunctionImpl;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

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
