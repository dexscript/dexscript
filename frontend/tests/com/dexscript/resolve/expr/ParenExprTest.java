package com.dexscript.resolve.expr;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.resolve.Denotation;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class ParenExprTest {

    @Test
    public void can_get_result() {
        Resolve resolve = new Resolve();
        resolve.define(new DexFunction("function Hello(): string { return 'hello' }"));
        Denotation.Type type = resolve.resolveType(DexExpr.parse("(Hello{})"));
        Assert.assertEquals("Hello", type.name());
    }
}
