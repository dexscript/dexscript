package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

public class UnionType extends Type {

    private final List<Type> types;

    public UnionType(Type type1, Type type2) {
        super(inferJavaClassName(type1, type2));
        types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
    }

    public UnionType(List<Type> types) {
        super(inferJavaClassName(types));
        this.types = types;
    }

    public List<Type> types() {
        return types;
    }

    @Override
    public Type union(Type that) {
        ArrayList<Type> types = new ArrayList<>(this.types);
        types.add(that);
        return new UnionType(types);
    }

    @Override
    public Type intersect(Type that) {
        if (that instanceof UnionType) {
            List<Type> thatTypes = ((UnionType) that).types;
            List<Type> union = new ArrayList<>();
            for (Type type : this.types) {
                if (thatTypes.contains(type)) {
                    union.add(type);
                }
            }
            return new UnionType(union);
        }
        return super.intersect(that);
    }

    @Override
    public boolean isAssignableFrom(Type that) {
        for (Type type : types) {
            if (type.isAssignableFrom(that)) {
                return true;
            }
        }
        return false;
    }

    private static String inferJavaClassName(Type type1, Type type2) {
        if (type1.javaClassName().equals(type2.javaClassName())) {
            return type1.javaClassName();
        }
        return "Object";
    }

    private static String inferJavaClassName(List<Type> types) {
        String javaClassName = types.get(0).javaClassName();
        for (Type type : types) {
            if (!type.javaClassName().equals(javaClassName)) {
                return "Object";
            }
        }
        return javaClassName;
    }
}
