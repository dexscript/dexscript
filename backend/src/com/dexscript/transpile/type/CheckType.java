package com.dexscript.transpile.type;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.type.JavaSuperTypeArgs;
import com.dexscript.type.Type;

import java.util.*;

public class CheckType {

    private final List<Class> javaClasses;
    private final List<Class> javaInterfaces;
    private final Map<Type, String> generated = new HashMap<>();

    public CheckType(List<Class> javaClasses, List<Class> javaInterfaces) {
        this.javaClasses = javaClasses;
        this.javaInterfaces = javaInterfaces;
    }

    public String $(Gen g, Type type) {
        String typeCheck = generated.get(type);
        if (typeCheck != null) {
            return typeCheck;
        }
        CheckType.Handler handler = handlers.get(type.getClass());
        if (handler == null) {
            ON_UNKNOWN_TYPE.handle(type);
            return null;
        }
        typeCheck = handler.handle(g, type, javaClasses, javaInterfaces);
        generated.put(type, typeCheck);
        return typeCheck;
    }


    interface Handler<E extends Type> {
        String handle(Gen g, E type, List<Class> javaClasses, List<Class> javaInterfaces);
    }

    interface OnUnknownType {
        void handle(Type type);
    }

    public static OnUnknownType ON_UNKNOWN_TYPE = type -> {
        throw new UnsupportedOperationException("not implemented: " + type.getClass());
    };


    private static final Map<Class<? extends Type>, Handler> handlers = new HashMap<Class<? extends Type>, Handler>() {

        {
            add(new CheckStringType());
            add(new CheckStringLiteralType());
            add(new CheckInt64Type());
            add(new CheckActorType());
        }

        private void add(Handler<?> handler) {
            put((Class<? extends Type>) JavaSuperTypeArgs.$(handler.getClass())[0], handler);
        }
    };
}
