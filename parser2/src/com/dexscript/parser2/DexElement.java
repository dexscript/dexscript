package com.dexscript.parser2;

import com.dexscript.parser2.core.Text;

public interface DexElement {

    Text src();
    int begin();
    int end();
    boolean matched();
    DexError err();

    static String describe(DexElement elem) {
        if (elem.matched()) {
            return new Text(elem.src().bytes, elem.begin(), elem.end()).toString();
        }
        return "<unmatched>" + elem.src() + "</unmatched>";
    }
}
