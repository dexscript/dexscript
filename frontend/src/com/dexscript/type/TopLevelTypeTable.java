package com.dexscript.type;

import java.util.HashMap;
import java.util.Map;

public class TopLevelTypeTable implements InterfaceType.ResolveType {

    protected final Map<String, TopLevelType> defined = new HashMap<>();

    public TopLevelTypeTable() {
    }

    public TopLevelTypeTable(TopLevelTypeTable copiedFrom) {
        defined.putAll(copiedFrom.defined);
    }

    @Override
    public Type resolveType(String name) {
        TopLevelType type = defined.get(name);
        if (type == null) {
            return BuiltinTypes.UNDEFINED;
        }
        return type;
    }

    public void define(TopLevelType type) {
        defined.put(type.name(), type);
    }
}
