package com.dexscript.type.core;

public class TypeDebugLog {

    public static void on() {
        FunctionTable.ON_FUNCTION_DEFINED = func -> System.out.println("function defined: " + func);
    }
}
