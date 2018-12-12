package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public abstract class NamedType extends Type {

    @NotNull
    private final String name;

    public NamedType(@NotNull String name, String javaClassName) {
        super(javaClassName);
        this.name = name;
    }

    @NotNull
    public final String name() {
        return name;
    }
}
