package com.dexscript.type;

import java.util.List;

public interface GenericType {

    Type generateType(List<Type> typeArgs);

    // only covariant is supported
    List<Type> typeParameters();
}
