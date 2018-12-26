package com.dexscript.ast;

import com.dexscript.test.framework.TestFramework;
import org.junit.Test;

public class DexFileTest {

    @Test
    public void one_actor() {
        TestFramework.assertParsedAST(DexFile::$);
    }

    @Test
    public void two_actors() {
        TestFramework.assertParsedAST(DexFile::$);
    }

    @Test
    public void skip_garbage() {
        TestFramework.assertParsedAST(DexFile::$);
    }

    @Test
    public void leaving_src_unparsed() {
        TestFramework.assertParsedAST(DexFile::$);
    }
}
