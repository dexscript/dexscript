package com.dexscript.type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TypeComparisonContext {

    private final TypeComparisonContext parent;
    private final Map<Type, Type> substituted;
    private final Set<FunctionType> undefined = new HashSet<>();
    private final String logPrefix;

    public TypeComparisonContext(TypeComparisonContext parent) {
        this.parent = parent;
        substituted = new HashMap<>();
        logPrefix = parent.logPrefix + "  ";
    }

    public TypeComparisonContext(Map<Type, Type> collector) {
        this.parent = null;
        substituted = collector;
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
        return false;
    }

    public void log(boolean assignable, Type to, Type from, String reason) {
        System.out.println(logPrefix + "[" + (assignable ? "assignable" : "not assignable") + "]" + " " + to + " | " + from + " | " + reason);
    }

    public void log(String msg) {
        System.out.println(logPrefix + msg);
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
}
