package com.dexscript.denotation;

import com.dexscript.ast.core.DexElement;
import org.jetbrains.annotations.NotNull;

public final class Value {

    private final String name;
    private final Type type;
    private final DexElement definedBy;

    public Value(@NotNull String name, @NotNull Type type, DexElement definedBy) {
        this.name = name;
        this.type = type;
        this.definedBy = definedBy;
    }

    public final String name() {
        return name;
    }

    public final Type type() {
        return type;
    }

    public final DexElement definedBy() {
        return definedBy;
    }
}
