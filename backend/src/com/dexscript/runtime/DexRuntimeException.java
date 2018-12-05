package com.dexscript.runtime;

public class DexRuntimeException extends RuntimeException {

    public DexRuntimeException() {
        super();
    }

    public DexRuntimeException(String message) {
        super(message);
    }

    public DexRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DexRuntimeException(Throwable cause) {
        super(cause);
    }

    protected DexRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
