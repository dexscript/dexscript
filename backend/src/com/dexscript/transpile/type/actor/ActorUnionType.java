package com.dexscript.transpile.type.actor;

import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActorUnionType implements NamedType {

    private final TypeSystem ts;
    private final String name;
    private final UnionType unionType;

    public ActorUnionType(TypeSystem ts, @NotNull String name, List<ActorType> types) {
        this.ts = ts;
        this.name = name;
        unionType = new UnionType(ts, (List) types);
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        return unionType.isAssignableFrom(ctx, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

}
