package com.dexscript.type;

import java.util.List;

public interface GenericType extends NamedType {

    Type generateType(List<Type> typeArgs);

    // only covariant is supported
    List<Type> typeParameters();


    default String describe(List<Type> typeArgs) {
        if (typeArgs == null) {
            typeArgs = typeParameters();
        }
        if (typeArgs.size() > 0) {
            StringBuilder desc = new StringBuilder(name());
            desc.append('<');
            for (int i = 0; i < typeArgs.size(); i++) {
                if (i > 0) {
                    desc.append(", ");
                }
                Type typeArg = typeArgs.get(i);
                desc.append(typeArg.toString());
            }
            desc.append('>');
            return desc.toString();
        }
        return name();
    }
}
