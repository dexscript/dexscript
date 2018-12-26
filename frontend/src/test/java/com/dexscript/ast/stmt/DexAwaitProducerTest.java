package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexAwaitProducerTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexAwaitProducer::$);
    }

    @Test
    public void missing_left_brace() {
        TestFramework.assertParsedAST(DexAwaitProducer::$);
    }

    @Test
    public void missing_right_brace() {
        TestFramework.assertParsedAST(DexAwaitProducer::$);
    }
}
