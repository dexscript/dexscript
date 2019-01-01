package com.dexscript.type;

import com.dexscript.ast.DexPackage;
import org.jetbrains.annotations.NotNull;

public interface NamedType extends DType {

    @NotNull
    String name();

    DexPackage pkg();

    default String qualifiedName() {
        return pkg() + "." + name();
    }
}
