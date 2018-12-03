package com.dexscript.denotation;

public interface BuiltinTypes {

    TopLevelType STRING = new TypeString();
    TopLevelType VOID = new TypeVoid();
    TopLevelType UNDEFINED = new TypeUndefined();

    TopLevelType[] TYPE_ARRAY = new TopLevelType[]{
            STRING,
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
