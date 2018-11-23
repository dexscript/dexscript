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

        public final String javaClassName;

        public Type(String name, TypeKind type, String javaClassName) {
            super(name, type);
            this.javaClassName = javaClassName;
            if (type == null) {
                throw new IllegalArgumentException("must specify kind for type denotation");
            }
        }
    }

    public static Type javaClass(String name, String javaClassName) {
        return new Type(name, TypeKind.JAVA, javaClassName);
    }

    public static class TypeKind extends Denotation {

        public static final TypeKind JAVA = new TypeKind("java");

        public TypeKind(String name) {
            super(name, null);
        }
    }
}
