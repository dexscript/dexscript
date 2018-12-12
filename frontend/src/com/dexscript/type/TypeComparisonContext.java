package com.dexscript.type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TypeComparisonContext {

    private final TypeComparisonContext parent;
    private final Map<Type, Type> substituted;
    private final Set<FunctionType> undefined = new HashSet<>();

    public TypeComparisonContext(TypeComparisonContext parent) {
        this.parent = parent;
        substituted = new HashMap<>();
    }

    public TypeComparisonContext(Map<Type, Type> collector) {
        this.parent = null;
        substituted = collector;
    }

    public void putSubstituted(Type key, Type value) {
        if (getSubstituted(key) != null) {
            throw new IllegalStateException();
        }
        substituted.put(key, value);
        substituted.put(value, key);
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
        parent.substituted.putAll(substituted);
        substituted.clear();
    }

    public void rollback() {
        substituted.clear();
    }

    public boolean shouldLog() {
        return false;
    }

    public void log(boolean assignable, Type to, Type from, String reason) {
        System.out.println("[" + (assignable ? "assignable" : "not assignable") + "]" + " " + to + " | " + from + " | " + reason);
    }

    public void undefine(FunctionType type) {
        undefined.add(type);
    }

    public boolean isUndefined(FunctionType type) {
        if (parent != null && parent.isUndefined(type)) {
            return true;
        }
        return undefined.contains(type);
    }
}
