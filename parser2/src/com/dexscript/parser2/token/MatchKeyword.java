package com.dexscript.parser2.token;

import com.dexscript.parser2.core.Text;

public class MatchKeyword {

    public static boolean __(Text src, int i, char b0) {
        if (i >= src.end) {
            return false;
        }
        return src.bytes[i] == b0;
    }

    public static boolean __(Text src, int i, char b0, char b1, char b2, char b3, char b4, char b5, char b6) {
        return src.bytes[i] == b0
                && src.bytes[i + 1] == b1
                && src.bytes[i + 2] == b2
                && src.bytes[i + 3] == b3
                && src.bytes[i + 4] == b4
                && src.bytes[i + 5] == b5
                && src.bytes[i + 6] == b6;
    }

}
