package com.dexscript.dispatch;

import com.dexscript.ast.DexFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActorEntry extends ImplEntry {

    private final DexFunction function;
    private final String canF;
    private final String newF;

    public ActorEntry(DispatchTable dispatchTable, DexFunction function, String canF, String newF) {
        super(canF, null, newF);
        function.attach(this);
        this.function = function;
        this.canF = canF;
        this.newF = newF;
        dispatchTable.add(virtualEntry(), this);
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
