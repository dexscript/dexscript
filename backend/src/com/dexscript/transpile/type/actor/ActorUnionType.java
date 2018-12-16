package com.dexscript.transpile.type.actor;

import com.dexscript.type.DType;
import com.dexscript.type.NamedType;
import com.dexscript.type.TypeComparisonContext;
import com.dexscript.type.UnionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActorUnionType implements NamedType {

    private final String name;
    private final UnionType unionType;

    public ActorUnionType(@NotNull String name, List<ActorType> types) {
        this.name = name;
        unionType = new UnionType((List) types);
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return unionType.isAssignableFrom(ctx, that);
    }

}
