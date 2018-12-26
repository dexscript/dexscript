package com.dexscript.ast.type;

import com.dexscript.ast.core.Text;
import com.dexscript.test.framework.TestFramework;
import org.junit.Assert;
import org.junit.Test;

public class DexVoidTypeTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexVoidType::$);
    }

    @Test
    public void unmatched() {
        TestFramework.assertParsedAST(DexVoidType::$);
    }

    @Test
    public void force_matched() {
        Assert.assertEquals("void", new DexVoidType(new Text("xxxx"), true).toString());
    }
}
