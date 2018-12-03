package com.dexscript.infer;

import com.dexscript.type.BuiltinTypes;

import java.util.HashMap;
import java.util.Map;

public class ValueTable {

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
        Value value = values.get(name);
        if (value == null) {
            return new Value(name, BuiltinTypes.UNDEFINED, null);
        }
        return value;
    }

    public void define(Value value) {
        values.put(value.name(), value);
    }
}
