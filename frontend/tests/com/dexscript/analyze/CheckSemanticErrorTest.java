package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class CheckSemanticErrorTest {

//    @Test
//    public void reference_not_existing_variable() {
//        String src = "package abc\n" +
//                "function Hello() {\n" +
//                "   return msg\n" +
//                "}";
//        CheckSemanticError result = new CheckSemanticError(new Resolve(), new DexFile(src));
//        Assert.assertTrue(result.hasError());
//    }
//
//    @Test
//    public void reference_not_existing_type() {
//        String src = "package abc\n" +
//                "function Hello(msg: int10) {\n" +
//                "}";
//        CheckSemanticError result = new CheckSemanticError(new Resolve(), new DexFile(src));
//        Assert.assertTrue(result.hasError());
//    }
//
//    @Test
//    public void return_type_mismatch() {
//        String src = "package abc\n" +
//                "function Hello(): string {\n" +
//                "   return 1\n" +
//                "}";
//        CheckSemanticError result = new CheckSemanticError(new Resolve(), new DexFile(src));
//        Assert.assertTrue(result.hasError());
//    }
//
//    @Test
//    public void deduce_expr_type_from_function() {
//        String src = "package abc\n" +
//                "function Hello(): string {\n" +
//                "   return ToString(1)\n" +
//                "}\n" +
//                "function ToString(i: int64): string {\n" +
//                "   return 'xxx'\n" +
//                "}";
//        DexFile file = new DexFile(src);
//        Resolve resolve = new Resolve();
//        resolve.define(file);
//        CheckSemanticError result = new CheckSemanticError(resolve, file);
//        Assert.assertFalse(result.hasError());
//    }
//
//    @Test
//    public void call_not_existing_function() {
//        String src = "package abc\n" +
//                "function Hello() {\n" +
//                "   NoSuchFunction()\n" +
//                "}";
//        CheckSemanticError result = new CheckSemanticError(new Resolve(), new DexFile(src));
//        Assert.assertTrue(result.hasError());
//    }
}
