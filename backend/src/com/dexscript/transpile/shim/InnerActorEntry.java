package com.dexscript.transpile.shim;

import com.dexscript.ast.stmt.DexAwaitConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class InnerActorEntry extends ConcreteEntry {

    private final DexAwaitConsumer awaitConsumer;
    private final String canF;
    private final String newF;
    private final Map<VirtualEntry, List<ConcreteEntry>> impls;
    private final String outerClassName;

    public InnerActorEntry(Map<VirtualEntry, List<ConcreteEntry>> impls, String outerClassName,
                           DexAwaitConsumer awaitConsumer, String canF, String newF) {
        super(canF, null, newF);
        this.impls = impls;
        this.outerClassName = outerClassName;
        awaitConsumer.attach(this);
        this.awaitConsumer = awaitConsumer;
        this.canF = canF;
        this.newF = newF;
        VirtualEntry virtualEntry = virtualEntry();
        List<ConcreteEntry> concreteEntries = impls.computeIfAbsent(virtualEntry, k -> new ArrayList<>());
        concreteEntries.add(this);
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
