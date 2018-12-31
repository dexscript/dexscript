package com.dexscript.transpile;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class ShortVarDeclTest {

    @Test
    public void string_const_widen_to_string() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void integer_const_widen_to_int64() {
        TestFramework.assertByList(TestTranspile::$);
    }
}
