package com.dexscript.denotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionTable implements TypeInterface.ResolveFunction {

    private final Map<String, List<TypeFunction>> defined = new HashMap<>();
    private final List<FunctionsProvider> providers = new ArrayList<>();

    public void define(TypeFunction function) {
        List<TypeFunction> functions = defined.computeIfAbsent(function.name(), k -> new ArrayList<>());
        functions.add(function);
    }

    public void lazyDefine(FunctionsProvider provider) {
        providers.add(provider);
    }

    @Override
    public boolean isDefined(TypeFunction that) {
        pullFromProviders();
        List<TypeFunction> functions = defined.get(that.name());
        if (functions == null) {
            return false;
        }
        for (TypeFunction function : functions) {
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
            for (TypeFunction function : provider.functions()) {
                define(function);
            }
        }
        providers.clear();
    }
}
