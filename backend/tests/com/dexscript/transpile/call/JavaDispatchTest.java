package com.dexscript.transpile.call;

import com.dexscript.transpile.Transpile;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class JavaDispatchTest {

    @Test
    public void new_java_class() {
        Object result = Transpile.$("" +
                "function Hello(): interface{} {" +
                "   return new File()\n" +
                "}");
        Assert.assertTrue(result instanceof File);
    }
}
