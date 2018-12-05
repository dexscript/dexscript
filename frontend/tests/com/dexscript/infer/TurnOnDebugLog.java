package com.dexscript.infer;

import com.dexscript.type.ResolveReturnType;

public interface TurnOnDebugLog {

    static void $() {
        InferType.Events.ON_UNKNOWN_ELEM = clazz -> {
            System.out.println("do not know how to handle type of " + clazz);
        };
        InferValue.Events.ON_UNKNOWN_ELEM = clazz -> {
            System.out.println("do not know how to handle value of " + clazz);
        };
        ResolveReturnType.Events.ON_MISSING_FUNCTION = (ts, funcName, args) -> {
            System.out.println("can not resolve " + funcName + " with " + args + " to a function");
        };
    }
}
