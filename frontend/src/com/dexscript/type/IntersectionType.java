package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

public class IntersectionType implements DType {

    private final TypeSystem ts;
    private final List<DType> members;

    public IntersectionType(TypeSystem ts, DType type1, DType type2) {
        this.ts = ts;
        members = new ArrayList<>();
        members.add(type1);
        members.add(type2);
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, DType that) {
        for (DType type : members) {
            if (!type.isAssignableFrom(ctx, that)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean _isSubType(IsAssignable ctx, DType that) {
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
