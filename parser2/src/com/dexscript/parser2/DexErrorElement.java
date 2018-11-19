package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;

public class DexErrorElement {

    public final Text src;
    public final int errorBegin;
    public final String[] expectations;

    public DexErrorElement(Text src, int errorBegin, String[] expectations) {
        this.src = src;
        this.errorBegin = errorBegin;
        this.expectations = expectations;
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("actual: ");
        msg.append('|');
        msg.append(new String(new byte[]{src.bytes[errorBegin]}));
        msg.append("|, ");
        msg.append("expected: ");
        for (String expectation : expectations) {
            msg.append(expectation);
            msg.append(", ");
        }
        msg.append("\n");
        return msg.toString();
    }
}
