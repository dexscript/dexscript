package com.dexscript.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopLevelTypeTable {

    protected final Map<String, TopLevelType> defined = new HashMap<>();
    private final List<TopLevelTypesProvider> providers = new ArrayList<>();

    public TopLevelTypeTable() {
    }

    public TopLevelTypeTable(TopLevelTypeTable copiedFrom) {
        defined.putAll(copiedFrom.defined);
    }

    public Type resolveType(String name) {
        pullFromProviders();
        TopLevelType type = defined.get(name);
        if (type == null) {
            return BuiltinTypes.UNDEFINED;
        }
        return type;
    }

    private void pullFromProviders() {
        if (providers.isEmpty()) {
            return;
        }
        for (TopLevelTypesProvider provider : providers) {
            for (TopLevelType type : provider.topLevelTypes()) {
                define(type);
            }
        }
        providers.clear();
    }

    public void define(TopLevelType type) {
        defined.put(type.name(), type);
    }

    public void lazyDefine(TopLevelTypesProvider provider) {
        providers.add(provider);
    }
}
