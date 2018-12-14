package com.dexscript.type;

public class TypeDebugLog {

    public static void on() {
        FunctionTable.ON_FUNCTION_DEFINED = func -> System.out.println("function defined: " + func);
        FunctionSig.ON_ARGUMENTS_COUNT_MISMATCH = (sig, args) -> System.out.println("sig mismatch due to params count mismatch, params: " + sig.params() + ", args: " + args);
        FunctionSig.ON_ARGUMENT_TYPE_MISMATCH = (sig, typeArgs, args, retHint, index, arg, param, sub) -> {
            System.out.println("sig mismatch due to #" + index + " param: " +
                    param + " is not assignable from " + arg + " with substitution: " + sub);
        };
    }
}
