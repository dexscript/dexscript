package com.dexscript.transpile.elem;

import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.transpile.OutClass;
import com.dexscript.transpile.method.OutAwaitConsumerMethod;

public class TranslateAwaitConsumer implements Translate<DexAwaitConsumer> {

    @Override
    public void handle(OutClass oClass, DexAwaitConsumer iAwaitConsumer) {
        new OutAwaitConsumerMethod(oClass, iAwaitConsumer);
    }
}
