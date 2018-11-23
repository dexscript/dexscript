package com.dexscript.parser2.core;

import java.util.ArrayList;
import java.util.List;

public interface DexElement {

    interface Visitor {
        void visit(DexElement elem);
    }

    class Collector implements Visitor {

        public final List<DexElement> collected = new ArrayList<>();

        @Override
        public void visit(DexElement elem) {
            if (elem == null) {
                throw new NullPointerException("expect not null element");
            }
            collected.add(elem);
        }
    }

    Text src();
    int begin();
    int end();
    boolean matched();
    DexError err();
    DexElement parent();

    void walkDown(Visitor visitor);
    default void walkUp(Visitor visitor) {
        visitor.visit(parent());
    }

    static String describe(DexElement elem) {
        if (!elem.matched()) {
            return "<unmatched>" + elem.src() + "</unmatched>";
        }
        if (elem.err() == null) {
            return new Text(elem.src().bytes, elem.begin(), elem.end()).toString();
        }
        int errorPos = elem.err().errorPos;
        if (errorPos < elem.begin()) {
            return "<error/>" + new Text(elem.src().bytes, elem.begin(), elem.end()).toString();
        }
        String part1 = new Text(elem.src().bytes, elem.begin(), errorPos).toString();
        String part2 = new Text(elem.src().bytes, errorPos, elem.end()).toString();
        return part1 + "<error/>" + part2;
    }
}
