package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PromiseTypeTest {

    @Test
    public void consume_any() {
        TypeSystem ts = new TypeSystem();
        DType promiseType = ts.resolveType("Promise");
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(promiseType.isAssignableFrom(inf));
    }

    @Test
    public void consume_string() {
        TypeSystem ts = new TypeSystem();
        DType promiseType = ts.resolveType("Promise", Arrays.asList(BuiltinTypes.STRING));
        InterfaceType consumeString = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(consumeString.isAssignableFrom(promiseType));
        InterfaceType consumeInt64 = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Consume__(): int64\n" +
                "}"));
        Assert.assertFalse(consumeInt64.isAssignableFrom(promiseType));
    }
}
