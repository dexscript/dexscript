package com.dexscript.parser2.core;

public interface DexLeafElement extends DexElement {
    @Override
    default void walkDown(Visitor visitor) {
    }
}
