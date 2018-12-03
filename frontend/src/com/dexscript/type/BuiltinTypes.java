package com.dexscript.type;

public interface BuiltinTypes {

    TopLevelType STRING = new StringType();
    TopLevelType INT64 = new Int64Type();
    TopLevelType VOID = new VoidType();
    TopLevelType UNDEFINED = new UndefinedType();

    TopLevelType[] TYPE_ARRAY = new TopLevelType[]{
            STRING,
            INT64,
            VOID
    };

    TopLevelTypeTable TYPE_TABLE = new TopLevelTypeTable() {

        {
            for (TopLevelType type : BuiltinTypes.TYPE_ARRAY) {
                defined.put(type.name(), type);
            }
        }

        @Override
        public void define(TopLevelType type) {
            throw new UnsupportedOperationException("builtin type table is readonly");
        }
    };
}
