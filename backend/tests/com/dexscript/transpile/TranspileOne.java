package com.dexscript.transpile;

import com.dexscript.runtime.Result;

import java.util.Map;

public interface TranspileOne {

    static Result __(String src) {
        try {
            Map<String, Class<?>> transpiled = new Transpiler()
                    .addFile("hello.ds", "package abc\n" + src)
                    .transpile();
            return (Result) transpiled.get("abc.Hello").getConstructor().newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Result __(String src, String arg1) {
        try {
            Map<String, Class<?>> transpiled = new Transpiler()
                    .addFile("hello.ds", "package abc\n" + src)
                    .transpile();
            return (Result) transpiled.get("abc.Hello").getConstructor(String.class).newInstance(arg1);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Result __(String src, Object arg1) {
        try {
            Map<String, Class<?>> transpiled = new Transpiler()
                    .addFile("hello.ds", "package abc\n" + src)
                    .transpile();
            return (Result) transpiled.get("abc.Hello").getConstructor(Object.class).newInstance(arg1);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
