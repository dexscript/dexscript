package com.dexscript.type;

public interface BuiltinTypes {

    Type ANY = new AnyType();
    NamedType BOOL = new BoolType();
    NamedType STRING = new StringType();
    NamedType INT64 = new Int64Type();
    NamedType INT32 = new Int32Type();
    NamedType UINT8 = new UInt8Type();
    NamedType VOID = new VoidType();
    NamedType UNDEFINED = new UndefinedType();

    NamedType[] TYPE_ARRAY = new NamedType[]{
            BOOL,
            STRING,
            UINT8,
            INT64,
            INT32,
            VOID
    };

    TypeTable TYPE_TABLE = new TypeTable() {
        {
            for (NamedType type : BuiltinTypes.TYPE_ARRAY) {
                define(type);
            }
        }
    };
}
