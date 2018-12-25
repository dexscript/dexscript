package com.dexscript.analyze;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class CheckSemanticErrorTest {

    private static DexFile parse(String src) {
        return new DexFile("package abc\n" + src);
    }

    private static boolean check(String src) {
        TypeSystem ts = new TypeSystem();
        DexFile file = parse(src);
        for (DexTopLevelDecl decl : file.topLevelDecls()) {
            if (decl.inf() != null) {
                ts.defineInterface(decl.inf());
            }
        }
        CheckSemanticError result = new CheckSemanticError(ts, file);
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

    @Test
    public void call_not_existing_function() {
        String src = "" +
                "function Hello() {\n" +
                "   world()\n" +
                "}";
        Assert.assertTrue(check(src));
    }

    @Test
    public void new_not_existing_function() {
        String src = "" +
                "function Hello() {\n" +
                "   new world()\n" +
                "}";
        Assert.assertTrue(check(src));
    }

    @Test
    public void assign_type_mismatch() {
        String src = "" +
                "function Hello() {\n" +
                "   var i: int64\n" +
                "   i = 'hello'\n" +
                "}";
        Assert.assertTrue(check(src));
    }

    @Test
    public void interface_not_implemented() {
        String src = "" +
                "interface MyObject {\n" +
                "   Say(): string\n" +
                "}\n" +
                "" +
                "function Hello() {\n" +
                "   var i: MyObject\n" +
                "   i = 'hello'\n" +
                "}";
        Assert.assertTrue(check(src));
    }

    @Test
    public void generic_interface() {
        String src = "" +
                "interface List {\n" +
                "   <E>: interface{}\n" +
                "   get(index: int32): E\n" +
                "}";
        Assert.assertFalse(check(src));
    }

    @Test
    public void generic_interface_referenced() {
        String src = "" +
                "interface List {\n" +
                "   <E>: interface{}\n" +
                "   get(index: int32): T\n" +
                "}";
        Assert.assertTrue(check(src));
    }
}
