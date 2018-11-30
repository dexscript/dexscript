package com.dexscript.ast.func;

import org.junit.Assert;
import org.junit.Test;

public class DexAwaitStmtTest {

    @Test
    public void empty() {
        Assert.assertEquals("await {}", new DexAwaitStmt("await {}").toString());
    }

    @Test
    public void await_consumer() {
        String src = "" +
                "await {\n" +
                "   case AA(): string {\n" +
                "       return 'hello'\n" +
                "   }\n" +
                "}";
        DexAwaitStmt stmt = new DexAwaitStmt(src);
        Assert.assertEquals(src, stmt.toString());
        Assert.assertEquals("case AA(): string {\n" +
                "       return 'hello'\n" +
                "   }", stmt.cases().get(0).toString());
    }

    @Test
    public void await_producer() {
        String src = "" +
                "await {\n" +
                "   case res := <-a {\n" +
                "   }\n" +
                "}";
        DexAwaitStmt stmt = new DexAwaitStmt(src);
        Assert.assertEquals(src, stmt.toString());
        Assert.assertEquals("case res := <-a {\n" +
                "   }", stmt.cases().get(0).toString());
    }
}
