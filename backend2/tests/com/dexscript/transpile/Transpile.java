package com.dexscript.transpile;

import com.dexscript.runtime.Result;

import java.util.Map;

public interface Transpile {

    static Result $(String src) {
        try {
            Map<String, Class<?>> oTown = new OutTown()
                    .addFile("hello.ds", "package abc\n" + src)
                    .transpile();
            return (Result) oTown.get("abc.Hello").getConstructor().newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
