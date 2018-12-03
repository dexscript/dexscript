package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public abstract class TopLevelType extends Type {

    @NotNull
    private final String name;

    public TopLevelType(@NotNull String name, String javaClassName) {
        super(javaClassName);
        this.name = name;
    }

    @NotNull
    public final String name() {
        return name;
    }
}
