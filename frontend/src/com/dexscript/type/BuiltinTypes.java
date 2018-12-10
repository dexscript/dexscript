package com.dexscript.type;

public interface BuiltinTypes {

    Type ANY = new AnyType();
    TopLevelType BOOL = new BoolType();
    TopLevelType STRING = new StringType();
    TopLevelType INT64 = new Int64Type();
    TopLevelType UINT8 = new UInt8Type();
    TopLevelType VOID = new VoidType();
    TopLevelType UNDEFINED = new UndefinedType();

    TopLevelType[] TYPE_ARRAY = new TopLevelType[]{
            BOOL,
            STRING,
            INT64,
            VOID
    };

    TopLevelTypeTable TYPE_TABLE = new TopLevelTypeTable() {
        {
            for (TopLevelType type : BuiltinTypes.TYPE_ARRAY) {
                define(type);
            }
        }
    };
}
