package com.dexscript.resolve;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.expr.DexFunctionCallExpr;
import com.dexscript.ast.inf.DexInfFunction;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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
        DexFunctionCallExpr callExpr = file.rootDecls().get(0).function().stmts().get(0)
                .asReturn().expr()
                .asFunctionCall();
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
        DexFunctionCallExpr callExpr = file.rootDecls().get(0).function().stmts().get(0)
                .asReturn().expr()
                .asFunctionCall();
        Denotation.FunctionType type = (Denotation.FunctionType) resolve.resolveFunction(callExpr);
        Assert.assertEquals("Long", type.ret().javaClassName());
    }

    @Test
    public void resolve_functions() {
        String src = "" +
                "function Hello(duck: Duck): string {" +
                "   return duck.Quack()\n" +
                "}\n" +
                "interface Duck {\n" +
                "   ::Quack(duck: Duck): string\n" +
                "}\n" +
                "function Quack(duck: string): string {\n" +
                "   return duck + ' quack'\n" +
                "}";
        DexFile file = new DexFile(src);
        Resolve resolve = new Resolve();
        resolve.declare(file);
        Denotation.Type string = (Denotation.Type) resolve.resolveType("string");
        Denotation.InterfaceType Duck = (Denotation.InterfaceType) resolve.resolveType("Duck");
        Assert.assertTrue(Duck.isAssignableFrom(string));
        Assert.assertFalse(string.isAssignableFrom(Duck));
        DexInfFunction infFunction = (DexInfFunction) Duck.definedBy().members().get(0);
        List<Denotation.FunctionType> functions = resolve.resolveFunctions(infFunction);
        Assert.assertEquals(1, functions.size());
        Assert.assertTrue(functions.get(0).isImpl());
    }
}
