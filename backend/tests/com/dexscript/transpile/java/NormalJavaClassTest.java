package com.dexscript.transpile.java;

import com.dexscript.transpile.Transpile;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class NormalJavaClassTest {

    @Test
    public void new_java_class() {
        File result = (File) Transpile.$("" +
                "function Hello(): interface{} {" +
                "   return new File('/tmp/test.txt')\n" +
                "}");
        Assert.assertEquals("test.txt", result.getName());
    }

    @Test
    public void call_java_method() {
        Boolean result = (Boolean) Transpile.$("" +
                "function Hello(): interface{} {" +
                "   var file: File\n" +
                "   file = new File('/tmp/test.txt')\n" +
                "   return file.canRead()\n" +
                "}");
        Assert.assertFalse(result);
    }
}