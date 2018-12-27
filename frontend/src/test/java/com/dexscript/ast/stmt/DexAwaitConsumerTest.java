package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexAwaitConsumerTest {

    @Test
    public void matched() {
        TestFramework.assertObject(DexAwaitConsumer::$);
    }

    @Test
    public void missing_identifier() {
        TestFramework.assertObject(DexAwaitConsumer::$);
    }

    @Test
    public void missing_blank() {
        TestFramework.assertObject(DexAwaitConsumer::$);
    }

    @Test
    public void missing_left_paren() {
        TestFramework.assertObject(DexAwaitConsumer::$);
    }

    @Test
    public void missing_right_paren() {
        TestFramework.assertObject(DexAwaitConsumer::$);
    }

    @Test
    public void missing_block() {
        TestFramework.assertObject(DexAwaitConsumer::$);
    }
}
