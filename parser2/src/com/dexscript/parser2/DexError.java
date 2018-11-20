package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;

public class DexError {

    public final Text src;
    public final int errorBegin;

    public DexError(Text src, int errorBegin) {
        this.src = src;
        this.errorBegin = errorBegin;
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("actual: ");
        msg.append('|');
        msg.append(new String(new byte[]{src.bytes[errorBegin]}));
        msg.append("|, ");
        msg.append("expected: ");
        msg.append("\n");
        return msg.toString();
    }
}
