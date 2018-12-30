package com.dexscript.ast;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexTopLevelDeclTest {

    @Test
    public void missing_left_paren() {
        TestFramework.assertParsedAST(DexTopLevelDecl::$);
    }

    @Test
    public void skip_garbage_in_prelude() {
        TestFramework.assertParsedAST(DexTopLevelDecl::$);
    }
}
