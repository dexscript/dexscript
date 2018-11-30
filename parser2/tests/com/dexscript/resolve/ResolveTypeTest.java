package com.dexscript.resolve;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Test;

public class ResolveTypeTest {

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
    public void reference_function_as_interface() {
        Resolve resolve = new Resolve();
        resolve.declare(new DexFunction("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}"));
        resolve.declare(new DexInterface("" +
                "interface StringPromise {\n" +
                "   GetResult__(): string" +
                "}"));
        Denotation.Type inf = (Denotation.Type) resolve.resolveType("Hello");
        Assert.assertEquals("Hello", inf.toString());
        Denotation.Type stringPromise = (Denotation.Type) resolve.resolveType("StringPromise");
        Assert.assertTrue(stringPromise.isAssignableFrom(inf));
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
