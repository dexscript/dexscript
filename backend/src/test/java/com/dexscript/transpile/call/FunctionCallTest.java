package com.dexscript.transpile.call;

import com.dexscript.test.framework.TestFramework;
import com.dexscript.transpile.TestTranspile;
import org.junit.Test;

public class FunctionCallTest {

    @Test
    public void call_without_arg() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void call_with_pos_arg() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void call_with_named_arg() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void function_without_return_value() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void invoke_int64() {
        TestFramework.assertByList(TestTranspile::$);
    }

    @Test
    public void invoke_int32() {
        TestFramework.assertByList(TestTranspile::$);
    }
}
