package com.dexscript.ast.inf;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexInfFunctionTest {

    @Test
    public void matched() {
        TestFramework.assertParsedAST(DexInfFunction::$);
    }
}
