package com.dexscript.shim.actor;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.ast.type.DexType;
import com.dexscript.shim.OutShim;
import com.dexscript.type.composite.InterfaceType;
import com.dexscript.type.core.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new OutShim(new TypeSystem()).typeSystem();
    }

    @Test
    public void resolve_any() {
        DType taskType = InferType.$(ts, null, DexType.$parse("Task"));
        InterfaceType inf = new InterfaceType(ts, DexInterface.$("" +
                "interface TaskString {\n" +
                "   Resolve__(value: string)\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, taskType));
    }

    @Test
    public void resolve_string() {
        DType taskType = InferType.$(ts, null, DexType.$parse("Task<string>"));
        InterfaceType resolveString = new InterfaceType(ts, DexInterface.$("" +
                "interface TaskString {\n" +
                "   Resolve__(value: string)\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(resolveString, taskType));
        InterfaceType resolveInt64 = new InterfaceType(ts, DexInterface.$("" +
                "interface TaskString {\n" +
                "   Resolve__(value: int64)\n" +
                "}"));
        Assert.assertFalse(IsAssignable.$(resolveInt64, taskType));
    }

    @Test(expected = DexSyntaxException.class)
    public void resolve_generic_type_with_wrong_arguments_count() {
        InferType.$(ts, null, DexType.$parse("Task<string, string>"));
    }

}
