package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionSigTest {

    @Test
    public void without_type_params() {
        FunctionSig sig = new FunctionSig(null, Arrays.asList(BuiltinTypes.STRING), BuiltinTypes.INT64);
        Assert.assertEquals(BuiltinTypes.INT64, sig.invoke(
                Collections.emptyList(), Arrays.asList(new StringLiteralType("abc")), null));
        Assert.assertEquals(BuiltinTypes.INT64, sig.invoke(
                Collections.emptyList(), Arrays.asList(BuiltinTypes.STRING), null));
        Assert.assertEquals(BuiltinTypes.UNDEFINED, sig.invoke(
                Collections.emptyList(), Arrays.asList(BuiltinTypes.INT64), null));
    }

    @Test
    public void invoke_with_type_args() {
        PlaceholderType T = new PlaceholderType("T");
        List<PlaceholderType> typeParams = Arrays.asList(T);
        List<Type> params = Arrays.asList(T);
        FunctionSig sig = new FunctionSig(typeParams, params, T);
        List<Type> args = Arrays.asList(BuiltinTypes.STRING);
        Type ret = sig.invoke(Collections.emptyList(), args, null);
        Assert.assertEquals(BuiltinTypes.STRING, ret);
    }
}
