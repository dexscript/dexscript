package com.dexscript.transpile.shim;

import com.dexscript.ast.func.DexAwaitConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class NestedActorEntry extends ConcreteEntry {

    private final DexAwaitConsumer awaitConsumer;
    private final String canF;
    private final String newF;

    public NestedActorEntry(Map<VirtualEntry, List<ConcreteEntry>> impls, DexAwaitConsumer awaitConsumer, String canF, String newF) {
        super(canF, null, newF);
        awaitConsumer.attach(this);
        this.awaitConsumer = awaitConsumer;
        this.canF = canF;
        this.newF = newF;
        VirtualEntry virtualEntry = virtualEntry();
        List<ConcreteEntry> concreteEntries = impls.computeIfAbsent(virtualEntry, k -> new ArrayList<>());
        concreteEntries.add(this);
    }
    private VirtualEntry virtualEntry() {
        return new VirtualEntry(awaitConsumer.identifier().toString(), awaitConsumer.params().size());
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
