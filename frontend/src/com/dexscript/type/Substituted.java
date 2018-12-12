package com.dexscript.type;

import java.util.HashMap;
import java.util.Map;

public final class Substituted {

    private final Substituted parent;
    private final Map<NamedType, Type> staging;

    public Substituted(Substituted parent) {
        this.parent = parent;
        staging = new HashMap<>();
    }

    public Substituted(Map<NamedType, Type> collector) {
        this.parent = null;
        staging = collector;
    }

    public void put(NamedType key, Type value) {
        if (get(key) != null) {
            throw new IllegalStateException();
        }
        staging.put(key, value);
        staging.put((NamedType) value, key);
    }

    public Type get(NamedType key) {
        if (staging.containsKey(key)) {
            return staging.get(key);
        }
        if (parent != null) {
            return parent.get(key);
        }
        return null;
    }

    public void commit() {
        if (parent == null) {
            throw new IllegalStateException();
        }
        parent.staging.putAll(staging);
        staging.clear();
    }

    public void rollback() {
        staging.clear();
    }
}
