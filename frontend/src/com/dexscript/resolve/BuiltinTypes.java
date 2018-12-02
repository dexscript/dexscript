package com.dexscript.resolve;

public class BuiltinTypes {
    public static final Denotation.Type UNDEFINED_TYPE = new Denotation.BuiltinType("undefined", null);
    public static final Denotation.Type STRING_TYPE = new Denotation.BuiltinType("string", "String");
    public static final Denotation.Type INT64_TYPE = new Denotation.BuiltinType("int64", "Long");
    public static final Denotation.Type FLOAT64_TYPE = new Denotation.BuiltinType("float64", "Double");
    public static final DenotationTable<Denotation.Type> BUILTIN_TYPES = new DenotationTable<Denotation.Type>()
            .add(STRING_TYPE)
            .add(FLOAT64_TYPE)
            .add(INT64_TYPE);
    public static final Denotation.Type RESULT_TYPE = new Denotation.BuiltinType("Result", "Result");
}
