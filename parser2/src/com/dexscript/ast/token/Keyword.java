package com.dexscript.ast.token;

import com.dexscript.ast.core.Text;

public interface Keyword {

    static boolean __(Text src, int i, char b0) {
        if (i >= src.end) {
            return false;
        }
        return src.bytes[i] == b0;
    }

    static boolean __(Text src, int i, char b0, char b1) {
        if (i >= src.end) {
            return false;
        }
        return src.bytes[i] == b0 && src.bytes[i + 1] == b1;
    }

    static boolean __(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5) {
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5;
    }

    static boolean __(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5, char b6) {
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5
                && src.bytes[i + 6] == b6;
    }

    static boolean __(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5, char b6, char b7) {
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5
                && src.bytes[i + 6] == b6
                && src.bytes[i + 7] == b7;
    }

    static boolean __(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5, char b6, char b7, char b8) {
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5
                && src.bytes[i + 6] == b6
                && src.bytes[i + 7] == b7
                && src.bytes[i + 8] == b8;
    }
}
