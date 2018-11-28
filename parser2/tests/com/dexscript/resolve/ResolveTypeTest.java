package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import org.junit.Assert;
import org.junit.Test;

public class ResolveTypeTest {

    @Test
    public void integer_literal_is_int64() {
        Assert.assertEquals(BuiltinTypes.INT64_TYPE, new Resolve().resolveType(DexExpr.parse("1")));
    }

    @Test
    public void float_literal_is_float64() {
        Assert.assertEquals(BuiltinTypes.FLOAT64_TYPE, new Resolve().resolveType(DexExpr.parse("1.1")));
    }

    @Test
    public void string_literal_is_string() {
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, new Resolve().resolveType(DexExpr.parse("'hello'")));
    }

    @Test
    public void reference_builtin_type() {
        DexFunction function = new DexFunction("function hello(): string {}");
        Denotation retType = new Resolve().resolveType(function.sig().ret());
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, retType);
    }

    @Test
    public void reference_interface() {
        DexInterface duckInf = new DexInterface("interface Duck{}");
        Resolve resolve = new Resolve();
        resolve.declare(duckInf);
        DexFunction function = new DexFunction("function hello(): Duck {}");
        Denotation retType = new Resolve().resolveType(function.sig().ret());
        Assert.assertEquals("Duck", retType.name());
    }

    @Test
    public void evaluate_add() {
        Resolve resolve = new Resolve();
        resolve.declare(new DexFunction("function Add__(i: int64, i: int64): int64 { return 0; }"));
        Assert.assertEquals(BuiltinTypes.INT64_TYPE, resolve.resolveType(DexExpr.parse("1+1")));
    }

    @Test
    public void evaluate_multi_dispatched_add() {
        Resolve resolve = new Resolve();
        resolve.declare(new DexFunction("function Add__(i: float64, i: float64): float64 { return 0.0; }"));
        resolve.declare(new DexFunction("function Add__(i: int64, i: int64): int64 { return 0; }"));
        Assert.assertEquals(BuiltinTypes.INT64_TYPE, resolve.resolveType(DexExpr.parse("1+1")));
    }

    @Test
    public void evaluate_argument_reference() {
        DexFunction function = new DexFunction("" +
                "function Hello(msg: string): string {\n" +
                "   return msg\n" +
                "}");
        Denotation type = new Resolve().resolveType(function.stmts().get(0).asReturn().expr());
        Assert.assertEquals(BuiltinTypes.STRING_TYPE, type);
    }
}
