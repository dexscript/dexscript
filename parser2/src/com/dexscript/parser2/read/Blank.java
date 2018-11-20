package com.dexscript.parser2.read;

public interface Blank {
    static boolean __(byte b) {
        return b == ' ' || b == '\t' || b == '\r' || b == '\n';
    }
}
