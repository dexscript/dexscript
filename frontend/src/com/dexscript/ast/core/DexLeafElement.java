package com.dexscript.ast.core;

public abstract class DexLeafElement extends DexElement {

    public DexLeafElement(Text src) {
        super(src);
    }

    @Override
    public final void walkDown(Visitor visitor) {
    }
}
