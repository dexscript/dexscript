package com.dexscript.infer;

import com.dexscript.ast.core.DexElement;
import com.dexscript.type.core.DType;
import org.jetbrains.annotations.NotNull;

public final class Value {

    private final String name;
    private final DType type;
    private final DexElement definedBy;

    public Value(@NotNull String name, DType type, DexElement definedBy) {
        this.name = name;
        this.type = type;
        this.definedBy = definedBy;
    }

    public final String name() {
        return name;
    }

    public final DType type() {
        return type;
    }

    public final DexElement definedBy() {
        return definedBy;
    }
}
