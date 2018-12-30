package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexUnionTypeTest {

    @Test
    public void union_of_two() {
        TestFramework.assertObject(DexType::$parse);
    }
}
