package com.dexscript.parser2.core;

public class DexParseException extends RuntimeException {
    public DexParseException() {
    }

    public DexParseException(String message) {
        super(message);
    }

    public DexParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DexParseException(Throwable cause) {
        super(cause);
    }

    public DexParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
