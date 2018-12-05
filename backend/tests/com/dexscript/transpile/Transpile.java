package com.dexscript.transpile;

import java.lang.reflect.Method;
import java.util.Map;

public interface Transpile {

    static Object $(String src, Object... args) {
        try {
            Map<String, Class<?>> oTown = new OutTown()
                    .addFile("hello.ds", "package abc\n" + src)
                    .transpile();
            Class<?> shimClass = oTown.get("com.dexscript.runtime.gen__.Shim__");
            Method newHello = shimClass.getMethod("Hello");
            return newHello.invoke(null, args);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
