package com.dexscript.transpile.call;

import com.dexscript.transpile.Transpile;
import com.dexscript.type.TypeDebugLog;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

public class JavaDispatchTest {

    @Test
    public void new_java_class() {
        File result = (File) Transpile.$("" +
                "function Hello(): interface{} {" +
                "   return new File('/tmp/test.txt')\n" +
                "}");
        Assert.assertEquals("test.txt", result.getName());
    }

    @Test
    public void new_generic_class() {
        TypeDebugLog.on();
        ArrayList result = (ArrayList) Transpile.$("" +
                "function Hello(): interface{} {" +
                "   return new ArrayList<int64>()\n" +
                "}");
        Assert.assertEquals(0, result.size());
    }
}
