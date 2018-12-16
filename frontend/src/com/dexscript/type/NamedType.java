package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

public interface NamedType extends DType {

    @NotNull
    String name();
}
