package com.dexscript.transpile.shim;

import com.dexscript.ast.DexFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ActorEntry extends ConcreteEntry {

    private final DexFunction function;
    private final String canF;
    private final String newF;

    public ActorEntry(Map<VirtualEntry, List<ConcreteEntry>> impls, DexFunction function, String canF, String newF) {
        super(canF, null, newF);
        function.attach(this);
        this.function = function;
        this.canF = canF;
        this.newF = newF;
        VirtualEntry virtualEntry = virtualEntry();
        List<ConcreteEntry> concreteEntries = impls.computeIfAbsent(virtualEntry, k -> new ArrayList<>());
        concreteEntries.add(this);
    }

    private VirtualEntry virtualEntry() {
        return new VirtualEntry(function.functionName(), function.params().size());
    }

    public DexFunction function() {
        return function;
    }

    public String canF() {
        return canF;
    }

    public String newF() {
        return newF;
    }
}
