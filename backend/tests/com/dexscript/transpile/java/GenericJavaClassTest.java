package com.dexscript.transpile.java;

import com.dexscript.transpile.Transpile;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.java.JClassType;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
                "   return TakeAny(list)\n" +
                "}\n" +
                "function TakeAny(obj: interface{}): string {" +
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

    @Test
    public void map_generic_interface() {
        JClassType type = new JClassType(oShim, List.class);
        Assert.assertEquals(1, type.typeParameters().size());
        Assert.assertEquals(ts.ANY, type.typeParameters().get(0));
        Assert.assertTrue(type.functions().size() > 1);
        Assert.assertFalse(IsAssignable.$(type, ts.UINT8));

        DType listOfInt64 = ResolveType.$(ts, "List<int64>");
        DType listOfString = ResolveType.$(ts, "List<string>");
        Assert.assertFalse(listOfInt64.isAssignableFrom(listOfString));
        Assert.assertFalse(listOfInt64.isAssignableFrom(ts.INT64));
    }

    @Test
    public void list_should_be_assignable_from_array_list() {
        TypeDebugLog.on();
        JClassType list = new JClassType(oShim, List.class);
        JClassType arrayList = new JClassType(oShim, ArrayList.class);
        Assert.assertTrue(list.isAssignableFrom(arrayList));

        DType listOfInt64 = ResolveType.$(ts, "List<int64>");
        DType arrayListOfInt64 = ResolveType.$(ts, "ArrayList<int64>");
        Assert.assertTrue(listOfInt64.isAssignableFrom(arrayListOfInt64));
    }
}
