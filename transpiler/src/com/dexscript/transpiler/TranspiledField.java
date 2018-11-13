package com.dexscript.transpiler;

public class TranspiledField {

    public final String originalName;
    public final String transpiledName;
    public final String type;

    public TranspiledField(String originalName, String transpiledName, String type) {
        this.originalName = originalName;
        this.transpiledName = transpiledName;
        this.type = type;
    }
}
