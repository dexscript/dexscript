package com.dexscript.transpile.java;

import com.dexscript.transpile.Transpile;
import com.dexscript.type.TypeDebugLog;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class GenericJavaClassTest {

    @Test
    public void new_generic_class() {
        ArrayList result = (ArrayList) Transpile.$("" +
                "function Hello(): interface{} {" +
                "   return new ArrayList<int64>()\n" +
                "}");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void multi_dispatch_with_generic_argument() {
        TypeDebugLog.on();
        // we can tell runtime java object implement List<int64> or List<interface{}>
        String result = (String) Transpile.$("" +
                "function Hello(): string {" +
                "   var list: List<int64>\n" +
                "   list = new ArrayList<int64>()\n" +
                "   return TakeList(list)\n" +
                "}\n" +
                "function TakeList(list: List<int64>): string {\n" +
                "   return 'matched List<int64>'\n" +
                "}\n" +
                "function TakeList(list: List<interface{}>): string {\n" +
                "   return 'matched List<interface{}>'\n" +
                "}");
        Assert.assertEquals("matched List<int64>", result);
    }
}
