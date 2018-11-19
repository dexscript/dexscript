package com.dexscript.parser2.read;

public class Blank {
    public static boolean __(byte b) {
        return b == ' ' || b == '\t' || b == '\r' || b == '\n';
    }
}
