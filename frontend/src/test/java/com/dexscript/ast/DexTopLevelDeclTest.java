package com.dexscript.ast;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexTopLevelDeclTest {

    @Test
    public void missing_left_paren() {
        TestFramework.assertObject(DexTopLevelDecl::$);
    }

    @Test
    public void skip_garbage_in_prelude() {
        TestFramework.assertObject(DexTopLevelDecl::$);
    }
}
