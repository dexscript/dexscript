package com.dexscript.resolve;

import com.dexscript.parser2.DexFunction;
import com.dexscript.parser2.expr.DexReference;
import com.dexscript.parser2.stmt.DexReturnStmt;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ResolveVarTest {

    @Test
    public void resolved() {
        String src = "" +
                "function hello(msg: string): string {\n" +
                "   return msg;\n" +
                "}";
        DexReturnStmt stmt = (DexReturnStmt) new DexFunction(src).block().stmts().get(0);
        List<Denotation> resolved = ResolveVar.__((DexReference) stmt.expr());
        Assert.assertEquals(1, resolved.size());
    }
}
