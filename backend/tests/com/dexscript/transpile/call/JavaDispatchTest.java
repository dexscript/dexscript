package com.dexscript.transpile.call;

import com.dexscript.runtime.Promise;
import com.dexscript.transpile.Transpile;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class JavaDispatchTest {

    @Test
    public void new_java_class() {
        Promise result = (Promise) Transpile.$("" +
                "function Hello(): interface{} {" +
                "   return new File()\n" +
                "}");
        Assert.assertTrue(result.value() instanceof File);
    }
}
