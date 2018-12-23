package com.dexscript.gen;

public class Indent {

    public final OP op;

    public Indent(OP op) {
        this.op = op;
    }

    public static interface OP {
        void apply();
    }
}
