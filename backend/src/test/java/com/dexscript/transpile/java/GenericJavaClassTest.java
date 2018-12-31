package com.dexscript.transpile.java;

import com.dexscript.transpile.TestTranspile;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class GenericJavaClassTest {

    @Test
    public void new_generic_class() {
        ArrayList result = (ArrayList) TestTranspile.$("" +
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
        // we can tell runtime java object implement List<int64> or List<interface{}>
        String result = (String) TestTranspile.$("" +
                "interface List {\n" +
                "   <E>: interface{}\n" +
                "   get(index: int32): E\n" +
                "}\n" +
                "function Hello(): string {\n" +
                "   str1 := TakeAny(new ArrayList<int64>())" +
                "   str2 := TakeAny(new ArrayList<string>())" +
                "   return str1 + ' ' + str2\n" +
                "}\n" +
                "function TakeAny(obj: interface{}): string {\n" +
                "   return TakeList(obj)\n" +
                "}\n" +
                "function TakeList(list: List<string>): string {\n" +
                "   return 'matched List<string>'\n" +
                "}\n" +
                "function TakeList(list: List<int64>): string {\n" +
                "   return 'matched List<int64>'\n" +
                "}\n" +
                "function TakeList(list: List<interface{}>): string {\n" +
                "   return 'matched List<interface{}>'\n" +
                "}\n" +
                "");
        Assert.assertEquals("matched List<int64> matched List<string>", result);
    }
}
