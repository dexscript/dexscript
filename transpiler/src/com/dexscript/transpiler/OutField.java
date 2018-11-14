package com.dexscript.transpiler;

public class OutField {

    public final String inName;
    public final String outName;
    public final String type;

    public OutField(String inName, String outName, String type) {
        this.inName = inName;
        this.outName = outName;
        this.type = type;
    }
}
