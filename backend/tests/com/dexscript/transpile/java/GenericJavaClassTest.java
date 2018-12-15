package com.dexscript.transpile.java;

import com.dexscript.transpile.Transpile;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.java.JavaClassType;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.TypeDebugLog;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void generic_class_functions() {
        JavaClassType type = new JavaClassType(new OutShim(new TypeSystem()), List.class);
        Assert.assertEquals(1, type.typeParameters().size());
        Assert.assertEquals(BuiltinTypes.ANY, type.typeParameters().get(0));
        Assert.assertTrue(type.functions().size() > 1);
        Assert.assertFalse(type.isAssignableFrom(BuiltinTypes.UINT8));
    }
}
