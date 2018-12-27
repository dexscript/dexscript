package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexTypeRefTest {

    @Test
    public void matched() {
        TestFramework.assertTrue(text -> DexTypeRef.$(text).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertFalse(text -> DexTypeRef.$(text).matched());
    }

    @Test
    public void package_qualifier() {
        TestFramework.assertObject(DexTypeRef::$);
    }

    @Test
    public void no_package_qualifier() {
        TestFramework.assertObject(DexTypeRef::$);
    }
}
