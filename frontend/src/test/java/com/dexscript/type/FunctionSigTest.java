package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.test.framework.FluentAPI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class FunctionSigTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void pos_param_assignable_from_arg() {
        testInvoke();
    }

    @Test
    public void pos_arg_assignable_from_param() {
        testInvoke();
    }

    @Test
    public void pos_args_count() {
        testInvoke();
    }

    @Test
    public void type_args_count() {
        testInvoke();
    }

    @Test
    public void infer_type_params() {
        testInvoke();
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
        InterfaceType ret = (InterfaceType) invoke(sig, args).func().ret();
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
        Assert.assertTrue(invoked.success());
        Assert.assertEquals("string", invoked.func().ret().toString());
    }

    @Test
    public void type_parameter_constraint_function() {
        FunctionSig sig = sig("(<T>: interface{}, left: T, right: T): bool");
        Assert.assertFalse(invoke(sig, resolve("string", "int64")).success());
    }

    @Test
    public void infer_with_return_value_hint() {
        FunctionSig sig = sig("(<T>: interface{}): T");
        DType ret = sig.invoke(Collections.emptyList(), resolve(), ts.ANY, ts.STRING).func().ret();
        Assert.assertEquals(ts.STRING, ret);
    }

    @Test
    public void specify_type_args() {
        FunctionSig sig = sig("(<T>: interface{}, left: T, right: T): bool");
        Assert.assertFalse(sig.invoke(
                resolve("int64"),
                resolve("string", "string"), ts.ANY, null).success());
    }

    @Test
    public void invoke_non_generic_function_with_type_args() {
        FunctionSig sig = sig("(left: T, right: T): bool");
        Assert.assertFalse(sig.invoke(
                resolve("int64"),
                resolve("string", "string"), ts.ANY, null).success());
    }

    @Test
    public void invoke_with_incompatible_context() {
        ts.defineInterface(DexInterface.$("" +
                "interface $ {\n" +
                "   GetPid(): string\n" +
                "}"));
        FunctionSig sig = sig("(): bool");
        FunctionSig.Invoked invoked = sig.invoke(
                Collections.emptyList(),
                Collections.emptyList(), ts.STRING, null);
        Assert.assertFalse(invoked.success());
    }

    @Test
    public void test_to_string() {
        Assert.assertEquals("(arg0: string): string", sig("(arg0: string) :string").toString());
        Assert.assertEquals("(arg0: string): void", sig("(arg0: string)").toString());
        Assert.assertEquals("(<T>: string, arg0: T): T", sig("(<T>: string, arg0: T): T").toString());
    }

    public FunctionSig.Invoked invoke(FunctionSig sig, List<DType> args) {
        FunctionSig.Invoked invoked = sig.invoke(Collections.emptyList(), args, ts.ANY, null);
        return invoked;
    }

    public void testInvoke() {
        FluentAPI testData = testDataFromMySection();
        FunctionSig sig = sig(testData.code());
        if ("typeArgs".equals(testData.table().head.get(0))) {
            testData.assertByTable((typeArgs, posArgs) -> sig.invoke(
                    resolveWithComma(typeArgs),
                    resolveWithComma(posArgs),
                    ts.ANY, null));
        } else {
            testData.assertByTable(posArgs -> invoke(sig, resolveWithComma(posArgs)));
        }
    }

    private void defineInterface(String src) {
        new InterfaceType(ts, DexInterface.$(src));
    }

    private FunctionSig sig(String src) {
        FunctionSig sig = new FunctionSig(ts, DexSig.$(src));
        sig.reparent(new FunctionType(ts, "", sig.params(), sig.ret(), sig));
        return sig;
    }

    private List<DType> resolveWithComma(String typeDefs) {
        if (typeDefs.isEmpty()) {
            return Collections.emptyList();
        }
        if (!typeDefs.startsWith("`")) {
            throw new RuntimeException("need to use `string` instead of string");
        }
        typeDefs = typeDefs.substring(1, typeDefs.length() - 1);
        return ResolveType.resolveTypes(ts, typeDefs.split(","));
    }

    private List<DType> resolve(String... typeDefs) {
        return ResolveType.resolveTypes(ts, typeDefs);
    }
}
