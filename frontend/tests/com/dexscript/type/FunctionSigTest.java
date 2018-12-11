package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class FunctionSigTest {

    @Test
    public void without_type_params() {
        FunctionSig sig = new FunctionSig(null, Arrays.asList(BuiltinTypes.STRING), BuiltinTypes.INT64);
        Assert.assertEquals(BuiltinTypes.INT64, sig.invoke(Arrays.asList(new StringLiteralType("abc"))));
        Assert.assertEquals(BuiltinTypes.INT64, sig.invoke(Arrays.asList(BuiltinTypes.STRING)));
        Assert.assertEquals(BuiltinTypes.UNDEFINED, sig.invoke(Arrays.asList(BuiltinTypes.INT64)));
    }
}
