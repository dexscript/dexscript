package com.dexscript.transpile;

public class Boat {

    private final Pier pier;
    private final String applyF;
    private final String checkF;

    public Boat(Pier pier, String qualifiedClassName, String funcName) {
        this.pier = pier;
        this.applyF = qualifiedClassName + "." + funcName;
        this.checkF = qualifiedClassName + ".can__" + funcName;
    }

    public Pier pier() {
        return pier;
    }

    public String applyF() {
        return applyF;
    }

    public String checkF() {
        return checkF;
    }
}
