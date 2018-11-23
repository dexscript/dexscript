package com.dexscript.analyzer;

import com.dexscript.parser2.DexFunction;
import org.junit.Assert;
import org.junit.Test;

public class CheckErrorTest {

    @Test
    public void no_error() {
        CheckError result = new CheckError(new DexFunction("function hello() {}"));
        Assert.assertFalse(result.hasError());
    }

    @Test
    public void has_error() {
        CheckError result = new CheckError(new DexFunction("function hello() }"));
        Assert.assertFalse(result.hasError());
    }
}
