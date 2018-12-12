package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public interface NamedType extends Type {

    @NotNull
    String name();
}
