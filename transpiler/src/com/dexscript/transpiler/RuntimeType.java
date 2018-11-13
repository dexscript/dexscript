package com.dexscript.transpiler;

public class RuntimeType {

    public final RuntimeTypeKind kind;
    public final String className;

    public RuntimeType(RuntimeTypeKind kind, String className) {
        this.kind = kind;
        this.className = className;
    }
}
