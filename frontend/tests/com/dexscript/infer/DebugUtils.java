package com.dexscript.infer;

public class DebugUtils {

    public static void turnOnDebugLog() {
        InferType.Events.ON_UNKNOWN_EXPR = clazz -> {
            System.out.println("do not know how to infer type of " + clazz);
        };
        InferFunctionCall.ON_MISSING_FUNCTION = (ts, callExpr, funcName, args) -> {
            System.out.println("can not resolve " + funcName + " with " + args + " to any function");
        };
    }
}
