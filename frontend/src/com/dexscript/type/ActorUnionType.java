package com.dexscript.type;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActorUnionType extends NamedType {

    private final UnionType unionType;

    public ActorUnionType(@NotNull String name, List<ActorType> types) {
        super(name, "Object");
        unionType = new UnionType((List) types);
    }

    @Override
    public boolean isAssignableFrom(Subs subs, Type that) {
        return unionType.isAssignableFrom(subs, that);
    }
}
