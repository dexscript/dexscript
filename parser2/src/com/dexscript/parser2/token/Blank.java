package com.dexscript.parser2.token;

public interface Blank {
    static boolean __(byte b) {
        return b == ' ' || b == '\t' || b == '\r' || b == '\n';
    }
}
