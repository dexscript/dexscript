package com.dexscript.denotation;

import com.dexscript.ast.expr.DexReference;

import java.util.HashMap;

public class TopLevelTypeTable extends HashMap<String, TopLevelType> implements TypeInterface.Resolve {

    public TopLevelTypeTable() {
    }

    public TopLevelTypeTable(TopLevelTypeTable copiedFrom) {
        super(copiedFrom);
    }

    @Override
    public Type resolveType(DexReference ref) {
        TopLevelType type = get(ref.toString());
        if (type == null) {
            return BuiltinTypes.UNDEFINED;
        }
        return type;
    }

    public void add(TopLevelType type) {
        put(type.name(), type);
    }
}
