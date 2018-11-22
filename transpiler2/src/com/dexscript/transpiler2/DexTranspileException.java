package com.dexscript.transpiler2;

public class DexTranspileException extends RuntimeException {

    public DexTranspileException() {
    }

    public DexTranspileException(String message) {
        super(message);
    }

    public DexTranspileException(String message, Throwable cause) {
        super(message, cause);
    }

    public DexTranspileException(Throwable cause) {
        super(cause);
    }

    public DexTranspileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
