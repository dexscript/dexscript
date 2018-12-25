package com.dexscript.ast.token;

public interface LineEnd {
    
    static boolean $(byte b) {
        return b == '\r' || b == '\n' || b == ';';
    }
}
