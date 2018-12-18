package com.dexscript.transpile.java;

import com.dexscript.transpile.Transpile;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.TypeDebugLog;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GenericJavaClassTest {

    private TypeSystem ts;
    private OutShim oShim;

    @Before
    public void setup() {
        ts = new TypeSystem();
        oShim = new OutShim(ts);
    }

    @Test
    public void new_generic_class() {
        ArrayList result = (ArrayList) Transpile.$("" +
                "interface List {\n" +
                "   <E>: interface{}\n" +
                "   get(index: int32): E\n" +
                "}\n" +
                "function Hello(): interface{} {\n" +
                "   var list: List<int64>\n" +
                "   list = new ArrayList<int64>()\n" +
                "   return list\n" +
                "}");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void multi_dispatch_with_generic_argument() {
        TypeDebugLog.on();
        // we can tell runtime java object implement List<int64> or List<interface{}>
        String result = (String) Transpile.$("" +
                "interface List {\n" +
                "   <E>: interface{}\n" +
                "   get(index: int32): E\n" +
                "}\n" +
                "function Hello(): string {\n" +
                "   var list: List<int64>\n" +
                "   list = new ArrayList<int64>()\n" +
                "   return TakeAny(list)\n" +
                "}\n" +
                "function TakeAny(obj: interface{}): string {\n" +
                "   return TakeList(obj)\n" +
                "}\n" +
                "function TakeList(list: List<int64>): string {\n" +
                "   return 'matched List<int64>'\n" +
                "}\n" +
                "function TakeList(list: List<string>): string {\n" +
                "   return 'matched List<string>'\n" +
                "}\n" +
                "");
        Assert.assertEquals("matched List<int64>", result);
    }
}
