package com.dexscript.ast.token;

import com.dexscript.ast.core.Text;

public interface Separator {
    static boolean $(Text src, int i) {
        if (i >= src.end) {
            return true;
        }
        byte b = src.bytes[i];
        if (A2Z.$(b) || Zero2Nine.$(b) || b == '_') {
            return false;
        }
        return true;
    }
}
