package com.dexscript.resolve;

public class Denotation {

    public final String name;
    public final Denotation type;

    public Denotation(String name, Denotation type) {
        this.name = name;
        this.type = type;
    }

    public static class Value extends Denotation {

        public Value(String name, Type type) {
            super(name, type);
            if (type == null) {
                throw new IllegalArgumentException("must specify type for value denotation");
            }
        }
    }

    public static class Type extends Denotation {

        public Type(String name, TypeKind type) {
            super(name, type);
            if (type == null) {
                throw new IllegalArgumentException("must specify kind for type denotation");
            }
        }
    }

    public static class TypeKind extends Denotation {

        public static final TypeKind BUILTIN = new TypeKind("builtin");

        public TypeKind(String name) {
            super(name, null);
        }
    }
}
