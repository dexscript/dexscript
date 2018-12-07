package com.dexscript.transpile.type;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.type.Type;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CheckType<E extends Type> {

    interface OnUnknownType {
        void handle(Type type);
    }

    class Events {
        public static OnUnknownType ON_UNKNOWN_TYPE = type -> {
            throw new UnsupportedOperationException("not implemented: " + type.getClass());
        };
    }

    String handle(Gen g, E type, List<Class> javaClasses, List<Class> javaInterfaces);

    Map<Class<? extends Type>, CheckType> handlers = new HashMap<Class<? extends Type>, CheckType>() {

        {
            add(new CheckStringType());
            add(new CheckStringLiteralType());
            add(new CheckInt64Type());
            add(new CheckActorType());
        }

        private void add(CheckType<?> handler) {
            ParameterizedType translateInf = (ParameterizedType) handler.getClass().getGenericInterfaces()[0];
            put((Class<? extends Type>) translateInf.getActualTypeArguments()[0], handler);
        }
    };

    static String $(Gen g, Type type, List<Class> javaClasses, List<Class> javaInterfaces) {
        CheckType checkType = handlers.get(type.getClass());
        if (checkType == null) {
            Events.ON_UNKNOWN_TYPE.handle(type);
            return null;
        }
        return checkType.handle(g, type, javaClasses, javaInterfaces);
    }
}
