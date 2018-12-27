package com.dexscript.ast.stmt;

import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexIdentifierTest {

    @Test
    public void matched() {
        TestFramework.assertTrue(src -> DexIdentifier.$(src).matched());
    }

    @Test
    public void unmatched() {
        TestFramework.assertFalse(src -> DexIdentifier.$(src).matched());
    }
}
