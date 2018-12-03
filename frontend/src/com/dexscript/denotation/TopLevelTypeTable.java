package com.dexscript.denotation;

import com.dexscript.ast.expr.DexReference;

import java.util.HashMap;
import java.util.Map;

public class TopLevelTypeTable implements TypeInterface.ResolveType {

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
