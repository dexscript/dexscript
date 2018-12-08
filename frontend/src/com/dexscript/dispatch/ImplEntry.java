package com.dexscript.dispatch;

public class ImplEntry {

    private final String canF;
    private final String callF;
    private final String newF;

    public ImplEntry(String canF, String callF, String newF) {
        this.canF = canF;
        this.callF = callF;
        this.newF = newF;
    }

    public static ImplEntry by(Class clazz) {
        String callF = clazz.getName() + ".call";
        return new ImplEntry(null, callF, callF);
    }

    public String canF() {
        return canF;
    }

    public String callF() {
        return callF;
    }

    public String newF() {
        return newF;
    }
}
