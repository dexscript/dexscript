package com.dexscript.type.core;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexIntersectionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IntersectionType implements DType {

    static {
        InferType.handlers.putAll(new HashMap<Class<? extends DexElement>, InferType>() {{
            put(DexIntersectionType.class, (ts, localTypeTable, elem) -> {
                DexIntersectionType intersectionType = (DexIntersectionType) elem;
                DType left = InferType.$(ts, localTypeTable, intersectionType.left());
                DType right = InferType.$(ts, localTypeTable, intersectionType.right());
                return left.intersect(right);
            });
        }});
    }

    private final TypeSystem ts;
    private final List<DType> members;

    public IntersectionType(TypeSystem ts, DType type1, DType type2) {
        this.ts = ts;
        members = new ArrayList<>();
        members.add(type1);
        members.add(type2);
    }

    @Override
    public boolean _isAssignable(IsAssignable ctx, DType that) {
        for (DType type : members) {
            if (!new IsAssignable(ctx, "intersection member", type, that).result()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    public List<DType> members() {
        return members;
    }
}
