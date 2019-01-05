package com.dexscript.shim.actor;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.gen.DeclareParams;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.OutShim;
import com.dexscript.shim.java.FunctionImpl;
import com.dexscript.type.core.FunctionType;

public class NewInnerActor extends FunctionImpl {

    private final DexAwaitConsumer awaitConsumer;
    private final String outerClassName;

    public NewInnerActor(OutShim oShim, FunctionType functionType, String outerClassName,
                         DexAwaitConsumer awaitConsumer) {
        super(oShim, functionType);
        this.outerClassName = outerClassName;
        this.awaitConsumer = awaitConsumer;
    }

    @Override
    public boolean hasAwait() {
        return true;
    }

    @Override
    protected String genCallF() {
        Gen g = oShim.g();
        String newF = oShim.allocateShim("new__" + functionType.name());
        g.__("public static Promise "
        ).__(newF);
        int paramsCount = functionType.params().size() + 1;
        DeclareParams.$(g, paramsCount, true);
        g.__(" {");
        g.__(new Indent(() -> {
            g.__(outerClassName
            ).__(" obj = ("
            ).__(outerClassName
            ).__(new Line(")arg0;"));
            g.__("return obj.new "
            ).__(awaitConsumer.identifier().toString()
            ).__("(scheduler");
            for (int i = 2; i < paramsCount; i++) {
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
