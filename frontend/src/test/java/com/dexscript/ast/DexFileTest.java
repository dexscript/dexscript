package com.dexscript.ast;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexFileTest {

    @Test
    public void one_actor() {
        TestFramework.assertObject(DexFile::$);
    }

    @Test
    public void two_actors() {
        TestFramework.assertObject(DexFile::$);
    }

    @Test
    public void skip_garbage() {
        TestFramework.assertObject(DexFile::$);
    }

    @Test
    public void leaving_src_unparsed() {
        TestFramework.assertObject(DexFile::$);
    }
}
