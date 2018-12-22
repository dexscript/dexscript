package com.dexscript.ast.core;

public class Text {

    public final byte[] bytes;
    public final int begin;
    public final int end;

    public Text(byte[] bytes, int begin, int end) {
        this.bytes = bytes;
        this.begin = begin;
        this.end = end;
    }

    public Text(byte[] bytes) {
        this.bytes = bytes;
        this.begin = 0;
        this.end = bytes.length;
    }

    public Text slice(int newBegin) {
        return new Text(bytes, newBegin, end);
    }

    public Text slice(int newBegin, int newEnd) {
        return new Text(bytes, newBegin, newEnd);
    }

    public Text(String src) {
        this(src.getBytes(), 0, src.getBytes().length);
    }

    @Override
    public String toString() {
        return new String(bytes, begin, end - begin);
    }

}
