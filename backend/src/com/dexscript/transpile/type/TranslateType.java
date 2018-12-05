package com.dexscript.transpile.type;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.type.Int64Type;
import com.dexscript.type.StringLiteralType;
import com.dexscript.type.StringType;
import com.dexscript.type.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TranslateType {

    interface OnUnknownType {
        void handle(Type type);
    }

    class Events {
        public static OnUnknownType ON_UNKNOWN_TYPE = type -> {
            throw new UnsupportedOperationException("not implemented: " + type.getClass());
        };
    }

    String handle(Gen g, Type type, List<Class> javaClasses, List<Class> javaInterfaces);

    Map<Class<? extends Type>, TranslateType> handlers = new HashMap<>() {{
        put(StringType.class, new TranslateStringType());
        put(StringLiteralType.class, new TranslateStringLiteralType());
        put(Int64Type.class, new TranslateInt64Type());
    }};

    static String $(Gen g, Type type, List<Class> javaClasses, List<Class> javaInterfaces) {
        TranslateType translate = handlers.get(type.getClass());
        if (translate == null) {
            Events.ON_UNKNOWN_TYPE.handle(type);
            return null;
        }
        return translate.handle(g, type, javaClasses, javaInterfaces);
    }
}
