package com.dexscript.transpile.skeleton;

import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.transpile.gen.*;

public class OutAwaitConsumerMethod implements OutMethod {

    private final Gen g;
    private final OutClass oClass;

    public OutAwaitConsumerMethod(OutClass oClass, DexAwaitConsumer iAwaitConsumer) {
        oClass.changeMethod(this);
        this.oClass = oClass;
        g = new Gen(oClass.indention());
        g.__("public Result "
        ).__(iAwaitConsumer.identifier().toString());
        DeclareParams.$(g, oClass.typeSystem(), iAwaitConsumer.produceSig());
        g.__(" {"
        ).__(new Indent(() -> {
            g.__("return new "
            ).__(iAwaitConsumer.identifier());
            InvokeParams.$(g, iAwaitConsumer.produceSig());
            g.__(";");
        }));
        g.__(new Line("}"));
        g.__(new OutInnerClass(oClass, iAwaitConsumer));
    }

    public OutClass oClass() {
        return oClass;
    }

    @Override
    public Gen g() {
        return g;
    }

    @Override
    public String finish() {
        return g.toString();
    }
}
