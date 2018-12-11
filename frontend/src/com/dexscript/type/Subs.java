package com.dexscript.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Subs {

    private final Map<PlaceholderType, List<Type>> subs = new HashMap<>();

    public void addSub(PlaceholderType placeholder, Type type) {
        subs.computeIfAbsent(placeholder, k -> new ArrayList<>()).add(type);
    }

    public void merge(Subs that) {
        for (Map.Entry<PlaceholderType, List<Type>> entry : that.subs.entrySet()) {
            for (Type type : entry.getValue()) {
                addSub(entry.getKey(), type);
            }
        }
    }

    public Map<PlaceholderType, Type> deduce() {
        Map<PlaceholderType, Type> deduced = new HashMap<>();
        for (Map.Entry<PlaceholderType, List<Type>> entry : subs.entrySet()) {
            Type deducedType = entry.getValue().get(0);
            for (Type type : entry.getValue()) {
                if (!deducedType.equals(type)) {
                    deducedType = BuiltinTypes.UNDEFINED;
                    break;
                }
            }
            deduced.put(entry.getKey(), deducedType);
        }
        return deduced;
    }
}
