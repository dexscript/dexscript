package com.dexscript.ast.token;

public interface One2Nine {

    static boolean $(byte b) {
        return ('1' <= b) && (b <= '9');
    }
}
