package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class CheckSemanticErrorTest {

    @Test
    public void reference_not_existing_variable() {
        String src = "package abc\n" +
                "function Hello() {\n" +
                "   return msg\n" +
                "}";
        CheckSemanticError result = new CheckSemanticError(new Resolve(), new DexFile(src));
        Assert.assertTrue(result.hasError());
    }

    @Test
    public void reference_not_existing_type() {
        String src = "package abc\n" +
                "function Hello(msg: int10) {\n" +
                "}";
        CheckSemanticError result = new CheckSemanticError(new Resolve(), new DexFile(src));
        Assert.assertTrue(result.hasError());
    }

    @Test
    public void return_type_mismatch() {
        String src = "package abc\n" +
                "function Hello(): string {\n" +
                "   return 1\n" +
                "}";
        CheckSemanticError result = new CheckSemanticError(new Resolve(), new DexFile(src));
        Assert.assertTrue(result.hasError());
    }
}
