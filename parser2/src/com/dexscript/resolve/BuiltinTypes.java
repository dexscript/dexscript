package com.dexscript.resolve;

public class BuiltinTypes {
    public static final Denotation.Type STRING_TYPE = Denotation.javaClass("string", "String");
    public static final Denotation.Type INT64_TYPE = Denotation.javaClass("int64", "Long");
    public static final DenotationTable BUILTIN_TYPES = new DenotationTable()
            .add(STRING_TYPE)
            .add(INT64_TYPE);
    public static final Denotation.Type RESULT_TYPE = Denotation.javaClass("Result", "Result");
}
