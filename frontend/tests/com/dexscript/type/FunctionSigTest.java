package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.elem.DexSig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FunctionSigTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    public FunctionSig.Invoked invoke(FunctionSig sig, List<DType> args) {
        FunctionSig.Invoked invoked = sig.invoke(null, args, null);
        return invoked;
    }

    @Test
    public void without_type_params() {
        FunctionSig sig = sig("(arg0: string): int64");
        Assert.assertTrue(invoke(sig, resolve("'abc'")).success());
        Assert.assertTrue(invoke(sig, resolve("string")).success());
        Assert.assertFalse(invoke(sig, resolve("int64")).success());
    }

    @Test
    public void infer_type_params() {
        FunctionSig sig = sig("(<T>: string, arg0: T): T");
        FunctionType func = invoke(sig, resolve("string")).function();
        Assert.assertEquals(ts.STRING, func.ret());
        Assert.assertEquals(ts.STRING, func.params().get(0));
    }

    @Test
    public void infer_nested_type_params() {
        defineInterface("" +
                "interface SomeInf {" +
                "   <T>: interface{}\n" +
                "   Get__(arg: T)\n" +
                "}");
        FunctionSig sig = sig("(<T>: string, arg0: SomeInf<T>): SomeInf<T>");
        List<DType> args = resolve("SomeInf<string>");
        InterfaceType ret = (InterfaceType) invoke(sig, args).function().ret();
        Assert.assertEquals(resolve("string"), ret.typeArgs());
    }

    @Test
    public void infer_deep_nested_type_params() {
        defineInterface("" +
                "interface SomeInf {" +
                "   <T>: interface{}\n" +
                "   Action1(arg: T)\n" +
                "}");
        defineInterface("" +
                "interface AnotherInf {" +
                "   <E1>: interface{}\n" +
                "   <E2>: interface{}\n" +
                "   Action2(index: '0', arg: E1)\n" +
                "   Action3(index: '1', arg: E2)\n" +
                "}");
        FunctionSig sig = sig("(<E1>: string, <E2>: string, arg0: AnotherInf<SomeInf<E1>, SomeInf<E2>>): E2");
        FunctionSig.Invoked invoked = invoke(sig, resolve("AnotherInf<SomeInf<'a'>, SomeInf<'b'>>"));
        invoked.dump();
        Assert.assertTrue(invoked.success());
        Assert.assertEquals("string", invoked.function().ret().toString());
    }

    @Test
    public void type_parameter_constraint_function() {
        FunctionSig sig = sig("(<T>: interface{}, left: T, right: T): bool");
        Assert.assertFalse(invoke(sig, resolve("string", "int64")).success());
    }

    @Test
    public void infer_with_return_value_hint() {
        FunctionSig sig = sig("(<T>: interface{}): T");
        DType ret = sig.invoke(null, resolve(), ts.STRING).function().ret();
        Assert.assertEquals(ts.STRING, ret);
    }

    @Test
    public void specify_type_args() {
        FunctionSig sig = sig("(<T>: interface{}, left: T, right: T): bool");
        Assert.assertFalse(sig.invoke(
                resolve("int64"),
                resolve("string", "string"), null).success());
    }

    @Test
    public void invoke_non_generic_function_with_type_args() {
        FunctionSig sig = sig("(left: T, right: T): bool");
        Assert.assertFalse(sig.invoke(
                resolve("int64"),
                resolve("string", "string"), null).success());
    }

    @Test
    public void test_to_string() {
        Assert.assertEquals("(string): string", sig("(arg0: string) :string").toString());
        Assert.assertEquals("(string): void", sig("(arg0: string)").toString());
        Assert.assertEquals("(<T>: string, T): T", sig("(<T>: string, arg0: T): T").toString());
    }

    private void defineInterface(String src) {
        new InterfaceType(ts, new DexInterface(src));
    }

    private FunctionSig sig(String src) {
        FunctionSig sig = new FunctionSig(ts, new DexSig(src));
        sig.reparent(new FunctionType(ts, "", sig.params(), sig.ret(), sig));
        return sig;
    }

    private List<DType> resolve(String... typeDefs) {
        return ResolveType.resolveTypes(ts, typeDefs);
    }
}
