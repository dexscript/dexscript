package com.dexscript.ast.token;

public interface LineEnd {
    static boolean __(byte b) {
        return b == '\r' || b == '\n' || b == ';';
    }
}
