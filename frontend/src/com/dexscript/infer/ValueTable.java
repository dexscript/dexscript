package com.dexscript.infer;

import java.util.HashMap;
import java.util.Map;

class ValueTable {

    private final Map<String, Value> values;

    public ValueTable() {
        values = new HashMap<>();
    }

    public ValueTable(ValueTable copiedFrom) {
        values = new HashMap<>(copiedFrom.values);
    }

    public ValueTable copy() {
        return new ValueTable(this);
    }

    public Value resolveValue(String name) {
        return values.get(name);
    }

    public void define(Value value) {
        values.put(value.name(), value);
    }
}
