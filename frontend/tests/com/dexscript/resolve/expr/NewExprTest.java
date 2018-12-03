package com.dexscript.resolve.expr;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.denotation.Type;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class NewExprTest {

//    @Test
//    public void can_consume() {
//        ResolveType resolve = new ResolveType();
//        resolve.define(new DexInterface("" +
//                "interface ConsumeInf {\n" +
//                "   Consume__(): string\n" +
//                "}"));
//        resolve.define(new DexFunction("function Hello(): string { return 'hello' }"));
//        Type actorType = resolve.resolveType(DexExpr.parse("Hello{}"));
//        Assert.assertEquals("Hello", actorType.name());
//        Type consumeInf = resolve.resolveType("ConsumeInf");
//        Assert.assertTrue(consumeInf.isAssignableFrom(actorType));
//        Assert.assertTrue(actorType.isAssignableFrom(consumeInf));
//    }
}
