package com.dexscript.transpiler;

import java.util.HashMap;
import java.util.Map;

public class Namer {

    private final Map<String, Integer> names = new HashMap<>();

    public String giveName(String suggestedName) {
        if (!names.containsKey(suggestedName)) {
            names.put(suggestedName, 1);
            return suggestedName;
        }
        int index = names.get(suggestedName) + 1;
        return suggestedName + index;
    }
}
