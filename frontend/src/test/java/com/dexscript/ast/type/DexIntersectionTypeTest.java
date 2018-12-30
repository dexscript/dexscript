package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexIntersectionTypeTest {

    @Test
    public void intersection_of_two(){
        TestFramework.assertObject(DexType::$parse);
    }
}
