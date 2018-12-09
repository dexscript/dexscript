package com.dexscript.transpile.shim.impl;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

public class CallInnerActor extends Impl {

    private final DexAwaitConsumer awaitConsumer;
    private final String canF;
    private final String newF;
    private final String outerClassName;
    private final boolean hasAwait;

    public CallInnerActor(FunctionType functionType, String outerClassName,
                          DexAwaitConsumer awaitConsumer, String canF, String newF, boolean hasAwait) {
        super(functionType, canF, null, newF);
        this.outerClassName = outerClassName;
        this.hasAwait = hasAwait;
        awaitConsumer.attach(this);
        this.awaitConsumer = awaitConsumer;
        this.canF = canF;
        this.newF = newF;
    }

    public String outerClassName() {
        return outerClassName;
    }

    public DexAwaitConsumer awaitConsumer() {
        return awaitConsumer;
    }

    public String canF() {
        return canF;
    }

    public String newF() {
        return newF;
    }

    @Override
    public boolean hasAwait() {
        return hasAwait;
    }

    protected void genNewF(Gen g) {
        String newF = OutShim.stripPrefix(newF());
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
            for (int i = 1; i < functionType().params().size(); i++) {
                g.__(", ");
                Type paramType = functionType().params().get(i);
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
