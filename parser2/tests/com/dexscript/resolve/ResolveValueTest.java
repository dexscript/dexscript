package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.ast.stmt.DexReturnStmt;
import org.junit.Assert;
import org.junit.Test;

public class ResolveValueTest {

    @Test
    public void resolved() {
        String src = "" +
                "function hello(msg: string): string {\n" +
                "   return msg;\n" +
                "}";
        DexReturnStmt stmt = (DexReturnStmt) new DexFunction(src).block().stmts().get(0);
        Denotation resolved = new ResolveValue().__((DexReference) stmt.expr());
        Assert.assertNotNull(resolved);
    }
}
