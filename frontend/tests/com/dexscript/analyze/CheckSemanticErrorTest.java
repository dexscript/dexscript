package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class CheckSemanticErrorTest {

    private static DexFile parse(String src) {
        return new DexFile("package abc\n" + src);
    }

    private static boolean check(String src) {
        CheckSemanticError result = new CheckSemanticError(new TypeSystem(), parse(src));
        return result.hasError();
    }

    @Test
    public void reference_not_existing_variable() {
        String src = "" +
                "function Hello() {\n" +
                "   return msg\n" +
                "}";
        Assert.assertTrue(check(src));
    }

    @Test
    public void reference_not_existing_type() {
        String src = "" +
                "function Hello(msg: int10) {\n" +
                "}";
        Assert.assertTrue(check(src));
    }

    @Test
    public void return_type_mismatch() {
        String src = "" +
                "function Hello(): string {\n" +
                "   return 1\n" +
                "}";
        Assert.assertTrue(check(src));
    }
//
//    @Test
//    public void deduce_expr_type_from_function() {
//        String src = "package example\n" +
//                "function Hello(): string {\n" +
//                "   return ToString(1)\n" +
//                "}\n" +
//                "function ToString(i: int64): string {\n" +
//                "   return 'xxx'\n" +
//                "}";
//        DexFile file = new DexFile(src);
//        ResolveType handle = new ResolveType();
//        handle.define(file);
//        CheckSemanticError result = new CheckSemanticError(handle, file);
//        Assert.assertFalse(result.hasError());
//    }
//
//    @Test
//    public void call_not_existing_function() {
//        String src = "package example\n" +
//                "function Hello() {\n" +
//                "   NoSuchFunction()\n" +
//                "}";
//        CheckSemanticError result = new CheckSemanticError(new ResolveType(), new DexFile(src));
//        Assert.assertTrue(result.hasError());
//    }
}
