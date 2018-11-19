package com.dexscript.parser2;

public class Text {

    public final byte[] bytes;
    public final int begin;
    public final int end;

    public Text(byte[] bytes, int begin, int end) {
        this.bytes = bytes;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return new String(bytes, begin, end - begin);
    }
}
