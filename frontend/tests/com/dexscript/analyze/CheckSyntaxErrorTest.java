package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import org.junit.Assert;
import org.junit.Test;

public class CheckSyntaxErrorTest {

    private static DexFile parse(String src) {
        return new DexFile("package abc\n" + src);
    }

    @Test
    public void no_error() {
        CheckSyntaxError result = new CheckSyntaxError(parse("function hello() {}"));
        Assert.assertFalse(result.hasError());
    }

    @Test
    public void has_error() {
        CheckSyntaxError result = new CheckSyntaxError(parse("function hello() }"));
        Assert.assertFalse(result.hasError());
    }
}
