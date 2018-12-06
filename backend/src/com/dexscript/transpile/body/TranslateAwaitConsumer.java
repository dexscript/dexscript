package com.dexscript.transpile.body;

import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.transpile.skeleton.OutClass;
import com.dexscript.transpile.skeleton.OutAwaitConsumerMethod;

public class TranslateAwaitConsumer implements Translate<DexAwaitConsumer> {

    @Override
    public void handle(OutClass oClass, DexAwaitConsumer iAwaitConsumer) {
        new OutAwaitConsumerMethod(oClass, iAwaitConsumer);
    }
}
