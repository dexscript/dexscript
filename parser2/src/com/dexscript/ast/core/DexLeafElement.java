package com.dexscript.ast.core;

public interface DexLeafElement extends DexElement {
    @Override
    default void walkDown(Visitor visitor) {
    }
}
