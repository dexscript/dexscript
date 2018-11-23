package com.dexscript.resolve;

public class Denotation {

    // the type of type
    public static final Denotation TYPE_TYPE = new Denotation("type", null);

    public final String name;
    public final Denotation type;

    public Denotation(String name, Denotation type) {
        this.name = name;
        this.type = type;
    }
}
