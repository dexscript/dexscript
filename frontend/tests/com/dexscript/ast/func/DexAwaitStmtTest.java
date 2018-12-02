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
        Assert.assertEquals(stmt, stmt.cases().get(0).parent());
    }

    @Test
    public void await_exit() {
        String src = "" +
                "await {\n" +
                "   exit!\n" +
                "}";
        DexAwaitStmt stmt = new DexAwaitStmt(src);
        Assert.assertEquals(src, stmt.toString());
        Assert.assertEquals("exit!", stmt.cases().get(0).toString());
    }

    @Test
    public void await_multiple_cases() {
        String src = "" +
                "await {\n" +
                "   case AA(): string {\n" +
                "       return 'hello'\n" +
                "   }\n" +
                "   case res := <-a {\n" +
                "   }\n" +
                "   exit!\n" +
                "}";
        DexAwaitStmt stmt = new DexAwaitStmt(src);
        Assert.assertEquals(src, stmt.toString());
        Assert.assertEquals(3, stmt.cases().size());
    }

    @Test
    public void recover_from_invalid_statement() {
        String src = "" +
                "await {\n" +
                "   case AA(): string {\n" +
                "       return 'hello'\n" +
                "   }??\n" +
                "   case res := <-a {\n" +
                "   }\n" +
                "   exit!\n" +
                "}";
        DexAwaitStmt stmt = new DexAwaitStmt(src);
        Assert.assertEquals("" +
                "await {\n" +
                "   case AA(): string {\n" +
                "       return 'hello'\n" +
                "   }<error/>??\n" +
                "   case res := <-a {\n" +
                "   }\n" +
                "   exit!\n" +
                "}", stmt.toString());
        Assert.assertEquals(4, stmt.cases().size());
    }
}
