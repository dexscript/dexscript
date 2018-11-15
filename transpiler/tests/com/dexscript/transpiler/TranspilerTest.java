package com.dexscript.transpiler;

import com.dexscript.runtime.Result1;
import org.junit.After;
import org.junit.Before;

public class TranspilerTest {

    private Transpiler transpiler;

    @Before
    public void setup() {
        transpiler = new Transpiler();
    }

    @After
    public void teardown() {
        transpiler.close();
    }

    protected Object transpile1(String source) {
        try {
            Class clazz = transpiler.transpile("hello", "package abc\n" + source).get("abc.Hello");
            return ((Result1) clazz.getConstructor().newInstance()).result1__();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void transpile0(String source) {
        try {
            Class clazz = transpiler.transpile("hello", "package abc\n" + source).get("abc.Hello");
            clazz.getConstructor().newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
