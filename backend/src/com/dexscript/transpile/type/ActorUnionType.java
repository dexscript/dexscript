package com.dexscript.transpile.type;

import com.dexscript.type.NamedType;
import com.dexscript.type.Type;
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
    public String javaClassName() {
        return Object.class.getCanonicalName();
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        return unionType.isAssignableFrom(ctx, that);
    }

}
