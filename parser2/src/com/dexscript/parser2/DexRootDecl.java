package com.dexscript.parser2;

import com.dexscript.parser2.core.DexElement;
import com.dexscript.parser2.core.DexError;
import com.dexscript.parser2.core.Text;

import java.util.Arrays;
import java.util.List;

public class DexRootDecl implements DexElement {

    private DexElement elem;

    public DexRootDecl(Text src) {
        elem = new DexFunction(src);
        if (elem.matched()) {
            return;
        }
    }

    @Override
    public Text src() {
        return elem.src();
    }

    @Override
    public int begin() {
        return elem.begin();
    }

    @Override
    public int end() {
        return elem.end();
    }

    @Override
    public boolean matched() {
        return elem.matched();
    }

    @Override
    public DexError err() {
        return elem.err();
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    public DexFunction function() {
        if (elem instanceof DexFunction) {
            return (DexFunction) elem;
        }
        return null;
    }

    public static List<DexRootDecl> parse(Text src) {
        DexRootDecl rootDecl = new DexRootDecl(src);
        return Arrays.asList(rootDecl);
    }
}
