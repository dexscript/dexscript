package com.dexscript.dispatch;

import com.dexscript.ast.stmt.DexAwaitConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InnerActorEntry extends ImplEntry {

    private final DexAwaitConsumer awaitConsumer;
    private final String canF;
    private final String newF;
    private final String outerClassName;

    public InnerActorEntry(DispatchTable dispatchTable, String outerClassName,
                           DexAwaitConsumer awaitConsumer, String canF, String newF) {
        super(canF, null, newF);
        this.outerClassName = outerClassName;
        awaitConsumer.attach(this);
        this.awaitConsumer = awaitConsumer;
        this.canF = canF;
        this.newF = newF;
        VirtualEntry virtualEntry = virtualEntry();
        dispatchTable.add(virtualEntry, this);
    }

    private VirtualEntry virtualEntry() {
        return new VirtualEntry(awaitConsumer.identifier().toString(), awaitConsumer.params().size() + 1);
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
}
