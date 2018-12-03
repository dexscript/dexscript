package com.dexscript.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable implements InterfaceType.ResolveFunction {

    private final Map<String, List<FunctionType>> defined = new HashMap<>();
    private final List<FunctionsProvider> providers = new ArrayList<>();

    public void define(FunctionType function) {
        List<FunctionType> functions = defined.computeIfAbsent(function.name(), k -> new ArrayList<>());
        functions.add(function);
    }

    public void lazyDefine(FunctionsProvider provider) {
        providers.add(provider);
    }

    @Override
    public boolean isDefined(FunctionType that) {
        pullFromProviders();
        List<FunctionType> functions = defined.get(that.name());
        if (functions == null) {
            return false;
        }
        for (FunctionType function : functions) {
            if (that.isAssignableFrom(function)) {
                return true;
            }
        }
        return false;
    }

    private void pullFromProviders() {
        if (providers.isEmpty()) {
            return;
        }
        for (FunctionsProvider provider : providers) {
            for (FunctionType function : provider.functions()) {
                define(function);
            }
        }
        providers.clear();
    }
}
