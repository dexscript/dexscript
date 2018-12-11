package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// part of FunctionType
class FunctionSig {

    private final List<PlaceholderType> typeParams;
    private final List<Type> params;
    private final Type ret;

    FunctionSig(List<PlaceholderType> typeParams, List<Type> params, Type ret) {
        this.typeParams = typeParams;
        this.params = params;
        this.ret = ret;
    }

    Type invoke(List<Type> args) {
        if (params.size() != args.size()) {
            return BuiltinTypes.UNDEFINED;
        }
        if (matchSig(args, params)) {
            return ret;
        }
        return BuiltinTypes.UNDEFINED;
    }

    private boolean matchSig(@NotNull List<Type> args, @NotNull List<Type> params) {
        for (int i = 0; i < params.size(); i++) {
            Type param = params.get(i);
            Type arg = args.get(i);
            boolean argMatched = arg.isAssignableFrom(param) || param.isAssignableFrom(arg);
            if (!argMatched) {
                return false;
            }
        }
        return true;
    }
}
