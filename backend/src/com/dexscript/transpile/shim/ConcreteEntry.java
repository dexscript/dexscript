package com.dexscript.transpile.shim;

class ConcreteEntry {

    private final String canF;
    private final String callF;
    private final String newF;

    public ConcreteEntry(String canF, String callF, String newF) {
        this.canF = canF;
        this.callF = callF;
        this.newF = newF;
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
