package com.dexscript.ast.expr;

public interface DexInvocationExpr {

    DexInvocation invocation();

    default boolean isInvokable() {
        return true;
    }
}
