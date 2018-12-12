package com.dexscript.type;

public class TypeDebugLog {

    public static void on() {
        FunctionSig.ON_INCOMPATIBLE_ARGUMENT = (sig, typeArgs, args, retHint, index, arg, param, sub) -> {
            System.out.println("sig mismatch due to #" + index + " param: " +
                    param + " is not assignable from " + arg + " with substitution: " + sub);
        };
    }
}
