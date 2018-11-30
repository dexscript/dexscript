package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitStmtTest {

    @Test
    public void empty() {
        Assert.assertEquals("await {}", new DexAwaitStmt("await {}").toString());
    }

    @Test
    public void case_serve() {
        String src = "" +
                "await {\n" +
                "   case AA(): string {\n" +
                "       return 'hello'\n" +
                "   }\n" +
                "}";
        DexAwaitStmt stmt = new DexAwaitStmt(src);
        Assert.assertEquals(src, stmt.toString());
    }
}
