package com.dexscript.type;

import java.util.*;

public final class TypeComparisonContext {

    private final TypeComparisonContext parent;
    private final Map<Type, Type> substituted;
    private final Set<FunctionType> undefined = new HashSet<>();
    private final String logPrefix;
    private final int logUntilLevelN;
    private final TypeComparisonCache cache;
    private final List<String> logCollector;

    public TypeComparisonContext(TypeComparisonContext parent) {
        this.parent = parent;
        this.logUntilLevelN = parent.logUntilLevelN - 1;
        this.cache = parent.cache;
        logCollector = parent.logCollector;
        substituted = new HashMap<>();
        logPrefix = parent.logPrefix + "  ";
    }

    public TypeComparisonContext(TypeComparisonCache cache, Map<Type, Type> substituted) {
        this(cache, substituted, 0, null);
    }

    public TypeComparisonContext(TypeComparisonCache cache, Map<Type, Type> substituted, int logUntilLevelN, List<String> logCollector) {
        this.cache = cache;
        this.logCollector = logCollector;
        this.parent = null;
        this.logUntilLevelN = logUntilLevelN;
        this.substituted = substituted;
        logPrefix = "";
    }

    public void putSubstituted(Type key, Type value) {
        substituted.put(key, value);
    }

    public Type getSubstituted(Type key) {
        if (substituted.containsKey(key)) {
            return substituted.get(key);
        }
        if (parent != null) {
            return parent.getSubstituted(key);
        }
        return null;
    }

    public void commit() {
        if (parent == null) {
            throw new IllegalStateException();
        }
        for (Map.Entry<Type, Type> entry : substituted.entrySet()) {
            if (entry.getKey() instanceof PlaceholderType) {
                if (shouldLog()) {
                    log("commit " + entry.getKey() + " => " + entry.getValue());
                }
                parent.substituted.put(entry.getKey(), entry.getValue());
            }
        }
        substituted.clear();
    }

    public void rollback() {
        substituted.clear();
    }

    public boolean shouldLog() {
        return logUntilLevelN > 0;
    }

    public void log(boolean assignable, Type to, Type from, String reason) {
        logCollector.add(String.format("%s[%s] %s | %s | %s",
                logPrefix, assignable ? "assignable" : "not assignable",
                to, from, reason));
    }

    public void log(String msg) {
        logCollector.add(logPrefix + msg);
    }

    public void undefine(FunctionType type) {
        undefined.add(type);
    }

    public boolean isUndefined(FunctionType type) {
        return undefined.contains(type);
    }

    public int levels() {
        if (parent == null) {
            return 0;
        }
        return parent.levels() + 1;
    }

    public TypeComparisonCache cache() {
        return cache;
    }
}
