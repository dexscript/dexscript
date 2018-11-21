package com.dexscript.parser2.core;

public interface DexElement {

    Text src();
    int begin();
    int end();
    boolean matched();
    DexError err();

    static String describe(DexElement elem) {
        if (!elem.matched()) {
            return "<unmatched>" + elem.src() + "</unmatched>";
        }
        if (elem.err() == null) {
            return new Text(elem.src().bytes, elem.begin(), elem.end()).toString();
        }
        String part1 = new Text(elem.src().bytes, elem.begin(), elem.err().errorPos).toString();
        String part2 = new Text(elem.src().bytes, elem.err().errorPos, elem.end()).toString();
        return part1 + "<error/>" + part2;
    }
}
