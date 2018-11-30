package com.dexscript.resolve.expr;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.resolve.Denotation;
import com.dexscript.resolve.Resolve;
import org.junit.Assert;
import org.junit.Test;

public class NewExprTest {

    @Test
    public void can_get_result() {
        Resolve resolve = new Resolve();
        resolve.declare(new DexFunction("function Hello(): string { return 'hello' }"));
        Denotation.Type type = (Denotation.Type) resolve.resolveType(DexExpr.parse("Hello{}"));
        Assert.assertEquals("Hello", type.name());
    }
}
