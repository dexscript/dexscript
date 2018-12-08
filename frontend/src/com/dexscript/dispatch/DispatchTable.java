package com.dexscript.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatchTable {

    private final Map<VirtualEntry, List<ImplEntry>> impls = new HashMap<>();

    public Iterable<? extends Map.Entry<VirtualEntry, List<ImplEntry>>> entrySet() {
        return impls.entrySet();
    }

    public void add(VirtualEntry virtualEntry, ImplEntry implEntry) {
        List<ImplEntry> concreteEntries = impls.computeIfAbsent(virtualEntry, k -> new ArrayList<>());
        concreteEntries.add(implEntry);
    }
}
