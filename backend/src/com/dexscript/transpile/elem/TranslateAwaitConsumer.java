package com.dexscript.transpile.elem;

import com.dexscript.ast.func.DexAwaitConsumer;
import com.dexscript.ast.func.DexStatement;
import com.dexscript.transpile.method.OutAwaitConsumerMethod;
import com.dexscript.transpile.OutClass;

public class TranslateAwaitConsumer implements Translate<DexAwaitConsumer> {

    @Override
    public void handle(OutClass oClass, DexAwaitConsumer iAwaitConsumer) {
        new OutAwaitConsumerMethod(oClass, iAwaitConsumer);
        for (DexStatement stmt : iAwaitConsumer.stmts()) {
            Translate.$(oClass, stmt);
        }
    }
}
