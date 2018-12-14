package com.dexscript.transpile.java;

import com.dexscript.transpile.Transpile;
import com.dexscript.type.TypeDebugLog;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class GenericJavaClassTest {

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
