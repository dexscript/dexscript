package com.dexscript.type;

import java.util.*;

public final class TypeComparisonContext {

    private final TypeComparisonContext parent;
    private final Map<DType, DType> permSub;
    private final Map<DType, DType> tempSub;
    private final Set<FunctionType> available;
    private final Set<FunctionType> unavailable;
    private final String logPrefix;
    private int logUntilLevelN;
    private List<String> logCollector;
    private TypeComparison comparing;

    public TypeComparisonContext(TypeComparisonContext parent) {
        this(parent, false);
    }

    public TypeComparisonContext(TypeComparisonContext parent, boolean isStaging) {
        this.parent = parent;
        this.logUntilLevelN = parent.logUntilLevelN - 1;
        logCollector = parent.logCollector;
        if (isStaging) {
            available = new HashSet<>(parent.available);
            unavailable = new HashSet<>(parent.unavailable);
            tempSub = new HashMap<>(parent.tempSub);
        } else {
            tempSub = new HashMap<>();
            available = new HashSet<>();
            unavailable = new HashSet<>();
        }
        permSub = parent.permSub;
        logPrefix = parent.logPrefix + "  ";
    }

    public TypeComparisonContext(Map<DType, DType> permSub) {
        this.parent = null;
        this.permSub = permSub;
        this.tempSub = permSub;
        this.available = new HashSet<>();
        this.unavailable = new HashSet<>();
        logPrefix = "";
    }

    public TypeComparisonContext logUntilLevelN(int N) {
        this.logUntilLevelN = N;
        return this;
    }

    public TypeComparisonContext logCollector(List<String> logCollector) {
        this.logCollector = logCollector;
        return this;
    }

    public void putSubstituted(DType key, DType value) {
        tempSub.put(key, value);
    }

    public DType getSubstituted(DType key) {
        if (permSub.containsKey(key)) {
            return permSub.get(key);
        }
        if (tempSub.containsKey(key)) {
            return tempSub.get(key);
        }
        return null;
    }

    public void commit() {
        if (parent == null) {
            throw new IllegalStateException();
        }
        for (Map.Entry<DType, DType> entry : tempSub.entrySet()) {
            if (entry.getKey() instanceof PlaceholderType) {
                if (shouldLog()) {
                    log("commit " + entry.getKey() + " => " + entry.getValue());
                }
                parent.tempSub.put(entry.getKey(), entry.getValue());
            }
        }
        tempSub.clear();
    }

    public boolean shouldLog() {
        return logUntilLevelN > 0;
    }

    public void log(boolean assignable, DType to, DType from, String reason) {
        logCollector.add(String.format("%s[%s] %s | %s | %s",
                logPrefix, assignable ? "assignable" : "not assignable",
                to, from, reason));
    }

    public void log(String msg) {
        logCollector.add(logPrefix + msg);
    }

    public void makeAvailable(FunctionType function) {
        available.add(function);
    }

    public void makeUnavailable(FunctionType function) {
        unavailable.add(function);
    }

    public boolean isAvailable(FunctionType function) {
        if (unavailable.contains(function)) {
            return false;
        }
        return available.contains(function) || function.impl() != null;
    }

    public int levels() {
        if (parent == null) {
            return 0;
        }
        return parent.levels() + 1;
    }

    public boolean isTopLevel() {
        return parent == null;
    }

    public TypeComparisonContext compare(DType to, DType from) {
        TypeComparison comparison = new TypeComparison(to, from);
        if (isComparing(comparison)) {
            dump();
            throw new IllegalStateException("is already checking " + to + " assignable from " + from);
        }
        this.comparing = comparison;
        return this;
    }

    private void dump() {
        if (comparing != null) {
            System.out.println(comparing);
        }
        if (parent == null) {
            return;
        }
        parent.dump();
    }

    private boolean isComparing(TypeComparison comparison) {
        if (comparing != null && comparing.equals(comparison)) {
            return true;
        }
        if (parent == null) {
            return false;
        }
        return parent.isComparing(comparison);
    }
}
