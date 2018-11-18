package com.dexscript.transpiler;

public class Boat {

    public final Pier pier;
    public final String apply;
    public final String check;

    public Boat(Pier pier, String qualifiedClassName, String funcName) {
        this.pier = pier;
        this.apply = qualifiedClassName + "." + funcName;
        this.check = qualifiedClassName + ".can__" + funcName;
    }
}
