package com.dexscript.ast.token;

public interface Zero2Nine {

    static boolean $(byte b) {
        return ('0' <= b) && (b <= '9');
    }
}
