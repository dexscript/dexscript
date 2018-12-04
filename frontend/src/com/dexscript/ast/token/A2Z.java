package com.dexscript.ast.token;

public interface A2Z {

    static boolean $(byte b) {
        return ('a' <= b && b <= 'z') || ('A' <= b && b <= 'Z');
    }
}
