package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.expr.DexCallExpr;
import org.junit.Assert;
import org.junit.Test;

public class ResolveFunctionTest {

    @Test
    public void resolve_function() {
        String src = "" +
                "package abc\n" +
                "function hello(): string {\n" +
                "   return A()\n" +
                "}\n" +
                "function A(): string{\n" +
                "   return 'a'\n" +
                "}";
        DexFile file = new DexFile(src);
        Resolve resolve = new Resolve();
        resolve.declare(file);
        DexCallExpr callExpr = file.rootDecls().get(0).function().stmts().get(0)
                .asReturn().expr()
                .asCall();
        Denotation.FunctionType type = (Denotation.FunctionType) resolve.resolveFunction(callExpr);
        Assert.assertEquals("String", type.ret().javaClassName());
    }

    @Test
    public void simple_multi_dispatching() {
        String src = "" +
                "package abc\n" +
                "function A(): int64 {\n" +
                "   return Add(1, 1)\n" +
                "}\n" +
                "function Add(x: float64, y: float64): float64 {\n" +
                "   return 0.0\n" +
                "}\n" +
                "function Add(x: int64, y: int64): int64 {\n" +
                "   return 0\n" +
                "}";
        DexFile file = new DexFile(src);
        Resolve resolve = new Resolve();
        resolve.declare(file);
        DexCallExpr callExpr = file.rootDecls().get(0).function().stmts().get(0)
                .asReturn().expr()
                .asCall();
        Denotation.FunctionType type = (Denotation.FunctionType) resolve.resolveFunction(callExpr);
        Assert.assertEquals("Long", type.ret().javaClassName());
    }
}
