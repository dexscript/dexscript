package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.denotation.BuiltinTypes;
import com.dexscript.denotation.Type;
import org.junit.Assert;
import org.junit.Test;

public class ResolveTypeTest {

//    @Test
//    public void reference_builtin_type() {
//        DexFunction function = new DexFunction("function hello(): string {}");
//        Denotation retType = new ResolveType().resolveType(function.sig().ret());
//        Assert.assertEquals(BuiltinTypes.STRING, retType);
//    }
//
//    @Test
//    public void reference_interface() {
//        DexInterface duckInf = new DexInterface("interface Duck{}");
//        ResolveType resolve = new ResolveType();
//        resolve.define(duckInf);
//        DexFunction function = new DexFunction("function hello(): Duck {}");
//        Denotation retType = new ResolveType().resolveType(function.sig().ret());
//        Assert.assertEquals("Duck", retType.name());
//    }
//
//    @Test
//    public void reference_function_as_interface() {
//        ResolveType resolve = new ResolveType();
//        resolve.define(new DexFunction("" +
//                "function Hello(): string {\n" +
//                "   return 'hello'\n" +
//                "}"));
//        resolve.define(new DexInterface("" +
//                "interface StringPromise {\n" +
//                "   GetResult__(): string" +
//                "}"));
//        Type inf = resolve.resolveType("Hello");
//        Assert.assertEquals("Hello", inf.toString());
//        Type stringPromise = resolve.resolveType("StringPromise");
//        Assert.assertTrue(stringPromise.isAssignableFrom(inf));
//    }
}
