package com.dexscript.transpile.skeleton;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.gen.*;

public class OutAwaitConsumerMethod implements OutMethod {

    private final Gen g;
    private final OutClass oClass;

    public OutAwaitConsumerMethod(OutClass oClass, DexAwaitConsumer iAwaitConsumer) {
        oClass.changeMethod(this);
        this.oClass = oClass;
        g = new Gen(oClass.indention());
        g.__(new OutInnerClass(oClass, iAwaitConsumer));
        new OutStateMethod(oClass);
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
