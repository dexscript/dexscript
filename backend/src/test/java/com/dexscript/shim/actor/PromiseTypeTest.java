package com.dexscript.shim.actor;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.type.DexType;
import com.dexscript.shim.OutShim;
import com.dexscript.type.composite.InterfaceType;
import com.dexscript.type.core.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PromiseTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new OutShim(new TypeSystem()).typeSystem();
    }

    @Test
    public void consume_any() {
        DType promiseType = InferType.$(ts, null, DexType.$parse("Promise"));
        InterfaceType inf = new InterfaceType(ts, DexInterface.$("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        IsAssignable isAssignable = new IsAssignable(promiseType, inf);
        Assert.assertTrue(isAssignable.result());
    }

    @Test
    public void consume_string() {
        DType promiseType = InferType.$(ts, null, DexType.$parse("Promise<string>"));
        InterfaceType consumeString = new InterfaceType(ts, DexInterface.$("" +
                "interface TaskString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(consumeString, promiseType));
        InterfaceType consumeInt64 = new InterfaceType(ts, DexInterface.$("" +
                "interface TaskString {\n" +
                "   Consume__(): int64\n" +
                "}"));
        Assert.assertFalse(IsAssignable.$(consumeInt64, promiseType));
    }
}
