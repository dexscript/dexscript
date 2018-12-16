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

    public DType invoke(FunctionSig sig, List<DType> args) {
        return sig.invoke(new Invocation("", null, args, null)).ret();
    }

    @Test
    public void without_type_params() {
        FunctionSig sig = sig("(arg0: string): int64");
        Assert.assertEquals(ts.INT64, invoke(sig, resolve("'abc'")));
        Assert.assertEquals(ts.INT64, invoke(sig, resolve("string")));
        Assert.assertEquals(ts.UNDEFINED, invoke(sig, resolve("int64")));
    }

    @Test
    public void infer_type_params() {
        FunctionSig sig = sig("(<T>: string, arg0: T): T");
        DType ret = invoke(sig, resolve("string"));
        Assert.assertEquals(ts.STRING, ret);
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
        InterfaceType ret = (InterfaceType) invoke(sig, args);
        Assert.assertEquals(resolve("string"), ret.typeArgs());
    }

    @Test
    public void infer_deep_nested_type_params() {
        defineInterface("" +
                "interface SomeInf {" +
                "   <T>: interface{}\n" +
                "   Get__(arg: T)\n" +
                "}");
        defineInterface("" +
                "interface AnotherInf {" +
                "   <E1>: interface{}\n" +
                "   <E2>: interface{}\n" +
                "   Get__(index: '0', arg: E1)\n" +
                "   Get__(index: '1', arg: E2)\n" +
                "}");
        FunctionSig sig = sig("(<E1>: string, <E2>: string, arg0: AnotherInf<SomeInf<E1>, SomeInf<E2>>): E2");
        DType ret = invoke(sig, resolve("AnotherInf<SomeInf<'a'>, SomeInf<'b'>>"));
        Assert.assertEquals("string", ret.toString());
    }

    @Test
    public void type_parameter_constraint_function() {
        FunctionSig sig = sig("(<T>: interface{}, left: T, right: T): bool");
        DType ret = invoke(sig, resolve("string", "int64"));
        Assert.assertEquals(ts.UNDEFINED, ret);
    }

    @Test
    public void infer_with_return_value_hint() {
        FunctionSig sig = sig("(<T>: interface{}): T");
        Invocation ivc = new Invocation("", null, resolve(), ts.STRING);
        DType ret = sig.invoke(ivc).ret();
        Assert.assertEquals(ts.STRING, ret);
    }

    @Test
    public void specify_type_args() {
        FunctionSig sig = sig("(<T>: interface{}, left: T, right: T): bool");
        Invocation ivc = new Invocation("",
                resolve("int64"),
                resolve("string", "string"), null);
        DType ret = sig.invoke(ivc).ret();
        Assert.assertEquals(ts.UNDEFINED, ret);
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

    private FunctionSig sig(String sig) {
        return new FunctionSig(ts, new DexSig(sig));
    }

    private List<DType> resolve(String... typeDefs) {
        return ResolveType.resolveTypes(ts, typeDefs);
    }
}
