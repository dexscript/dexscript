package com.dexscript.type.core;

import java.util.HashMap;
import java.util.Map;

public class TypeComparisonCache {

    private final Map<TypeComparison, Boolean> cache = new HashMap<>();

    public Boolean get(TypeComparison comparison) {
        return cache.get(comparison);
    }

    public void set(TypeComparison comparison, Boolean assignableFrom) {
        cache.put(comparison, assignableFrom);
    }
}
