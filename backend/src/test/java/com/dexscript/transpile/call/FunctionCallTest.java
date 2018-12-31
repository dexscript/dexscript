package com.dexscript.transpile.call;

import com.dexscript.test.framework.TestFramework;
import com.dexscript.transpile.Transpile;
import org.junit.Test;

public class FunctionCallTest {

    @Test
    public void call_without_arg() {
        TestFramework.assertByList(Transpile::$);
    }

    @Test
    public void call_with_pos_arg() {
        TestFramework.assertByList(Transpile::$);
    }

    @Test
    public void call_with_named_arg() {
        TestFramework.assertByList(Transpile::$);
    }

    @Test
    public void function_without_return_value() {
        TestFramework.assertByList(Transpile::$);
    }

    @Test
    public void invoke_int64() {
        TestFramework.assertByList(Transpile::$);
    }

    @Test
    public void invoke_int32() {
        TestFramework.assertByList(Transpile::$);
    }
}