package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexReference;
import org.junit.Assert;
import org.junit.Test;

public class ResolveValueTest {

//    @Test
//    public void resolve_argument() {
//        String src = "" +
//                "function hello(msg: string): string {\n" +
//                "   return msg;\n" +
//                "}";
//        DexReference ref = new DexFunction(src).stmts().get(0).asReturn().expr().asRef();
//        Denotation resolved = new ResolveType().resolveValue(ref);
//        Assert.assertNotNull(resolved);
//    }
//
//    @Test
//    public void resolve_short_var_decl() {
//        String src = "" +
//                "function hello(): string {\n" +
//                "   a := 'hello'\n" +
//                "   return a;\n" +
//                "}";
//        DexReference ref = new DexFunction(src).stmts().get(1).asReturn().expr().asRef();
//        Denotation resolved = new ResolveType().resolveValue(ref);
//        Assert.assertNotNull(resolved);
//    }
}
