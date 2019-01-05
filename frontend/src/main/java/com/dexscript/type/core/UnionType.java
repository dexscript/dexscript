package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexUnionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnionType implements DType {

    static {
        InferType.handlers.putAll(new HashMap<Class<? extends DexElement>, InferType>() {{
            put(DexUnionType.class, (ts, localTypeTable, elem) -> {
                DexUnionType unionType = (DexUnionType) elem;
                DType left = InferType.$(ts, localTypeTable, unionType.left());
                DType right = InferType.$(ts, localTypeTable, unionType.right());
                return left.union(right);
            });
        }});
    }

    private final TypeSystem ts;
    private final List<DType> members;

    public UnionType(TypeSystem ts, DType type1, DType type2) {
        this.ts = ts;
        members = new ArrayList<>();
        members.add(type1);
        members.add(type2);
    }

    public UnionType(TypeSystem ts, List<DType> types) {
        this.ts = ts;
        this.members = types;
    }

    public List<DType> members() {
        return members;
    }

    @Override
    public DType union(DType that) {
        ArrayList<DType> types = new ArrayList<>(this.members);
        types.add(that);
        return new UnionType(ts, types);
    }

    @Override
    public DType intersect(DType that) {
        if (that instanceof UnionType) {
            List<DType> thatTypes = ((UnionType) that).members;
            List<DType> union = new ArrayList<>();
            for (DType type : this.members) {
                if (thatTypes.contains(type)) {
                    union.add(type);
                }
            }
            return new UnionType(ts, union);
        }
        return new IntersectionType(ts, this, that);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        for (DType member : members) {
            if (new IsAssignable(ctx, "union member", member, that).result()) {
                return true;
            }
        }
        return false;
    }
}
