package com.dexscript.resolve;

import java.util.HashMap;
import java.util.Map;

public class DenotationTable<T extends Denotation> extends HashMap<String, T> {

    public DenotationTable() {
    }

    public DenotationTable(DenotationTable that) {
        super(that);
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> m) {
        if (m == null) {
            return;
        }
        super.putAll(m);
    }

    public DenotationTable<T> copy() {
        return new DenotationTable<T>(this);
    }

    public DenotationTable<T> add(T denotation) {
        put(denotation.name(), denotation);
        return this;
    }
}
