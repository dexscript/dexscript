package com.dexscript.runtime;

public class DexScriptException extends RuntimeException {

    public DexScriptException() {
    }

    public DexScriptException(String message) {
        super(message);
    }

    public DexScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public DexScriptException(Throwable cause) {
        super(cause);
    }

    public DexScriptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static RuntimeException reportMissingImplementation(String funcName, Object ...args) {
        StringBuilder msg = new StringBuilder("can not find implementation for function ");
        msg.append(funcName);
        msg.append(", args:");
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            msg.append(" (");
            msg.append(arg.getClass().getName());
            msg.append(")");
            msg.append(arg.toString());
        }
        throw new DexScriptException(msg.toString());
    }
}
