package com.dexscript.infer;

import com.dexscript.type.ResolveReturnType;

public interface TurnOnDebugLog {

    static void $() {
        InferType.Events.ON_UNKNOWN_EXPR = clazz -> {
            System.out.println("do not know how to infer type of " + clazz);
        };
        ResolveReturnType.Events.ON_MISSING_FUNCTION = (ts, funcName, args) -> {
            System.out.println("can not resolve " + funcName + " with " + args + " to a function");
        };
    }
}
