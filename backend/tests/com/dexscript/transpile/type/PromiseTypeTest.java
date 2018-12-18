package com.dexscript.transpile.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.type.DType;
import com.dexscript.type.InterfaceType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PromiseTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void consume_any() {
        DType promiseType = ResolveType.$(ts, "Promise");
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(promiseType.isAssignableFrom(inf));
    }

    @Test
    public void consume_string() {
        DType promiseType = ResolveType.$(ts, "Promise<string>");
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
