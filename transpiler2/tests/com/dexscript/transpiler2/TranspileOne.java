package com.dexscript.transpiler2;

import java.util.Map;

public interface TranspileOne {
    static Object __(String src) {
        try {

            Map<String, Class<?>> transpiled = new Town()
                    .addFile("hello.ds", "package abc\n" + src)
                    .transpile();
            return transpiled.get("abc.Hello").getConstructor().newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
