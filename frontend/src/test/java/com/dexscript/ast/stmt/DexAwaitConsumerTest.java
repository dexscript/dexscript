package com.dexscript.ast.stmt;

import com.dexscript.test.framework.TestFramework;
import org.junit.Assert;
import org.junit.Test;

public class DexAwaitConsumerTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexAwaitConsumer::$);
    }

    @Test
    public void missing_identifier() {
        TestFramework.assertParsedAST(DexAwaitConsumer::$);
    }

    @Test
    public void missing_blank() {
        TestFramework.assertParsedAST(DexAwaitConsumer::$);
    }

    @Test
    public void missing_left_paren() {
        TestFramework.assertParsedAST(DexAwaitConsumer::$);
    }

    @Test
    public void missing_right_paren() {
        TestFramework.assertParsedAST(DexAwaitConsumer::$);
    }

    @Test
    public void missing_block() {
        TestFramework.assertParsedAST(DexAwaitConsumer::$);
    }
}
