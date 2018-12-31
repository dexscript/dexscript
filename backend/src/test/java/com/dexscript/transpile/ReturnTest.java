package com.dexscript.transpile;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class ReturnTest {

    @Test
    public void return_string() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void return_integer_literal() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void return_int64() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void return_int32() {
        TestFramework.assertByList(TestTranspile::$);
    }
}
