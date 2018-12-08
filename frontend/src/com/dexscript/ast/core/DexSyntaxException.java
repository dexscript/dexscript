package com.dexscript.ast.core;

public class DexSyntaxException extends RuntimeException {

    public DexSyntaxException() {
    }

    public DexSyntaxException(String message) {
        super(message);
    }

    public DexSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public DexSyntaxException(Throwable cause) {
        super(cause);
    }

    public DexSyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
