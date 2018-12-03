package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActorUnionType extends TopLevelType {

    private final UnionType unionType;

    public ActorUnionType(@NotNull String name, List<ActorType> types) {
        super(name, "Object");
        unionType = new UnionType((List) types);
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        return unionType.isAssignableFrom(that);
    }
}
