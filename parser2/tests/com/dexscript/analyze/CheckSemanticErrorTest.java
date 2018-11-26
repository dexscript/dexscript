package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import org.junit.Assert;
import org.junit.Test;

public class CheckSemanticErrorTest {

    @Test
    public void reference_not_existing_variable() {
        String src = "package abc\n" +
                "function Hello() {\n" +
                "   return msg\n" +
                "}";
        CheckSemanticError result = new CheckSemanticError(new DexFile(src));
        Assert.assertTrue(result.hasError());
    }
}
