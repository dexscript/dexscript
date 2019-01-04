package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.infer.ResolvePosArgs;
import com.dexscript.test.framework.FluentAPI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.dexscript.test.framework.TestFramework.stripQuote;
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
    public void infer_one_direct_placeholder() {
        testInvoke();
    }

    @Test
    public void infer_one_parameterized_type() {
        testInvoke();
    }

    @Test
    public void infer_deep_nested_type_params() {
        testInvoke();
    }

    @Test
    public void infer_two_direct_placeholders() {
        testInvoke();
    }

    @Test
    public void infer_with_return_value_hint() {
        FunctionSig sig = sig("(<T>: interface{}): T");
        DType ret = sig.invoke(Collections.emptyList(), Collections.emptyList(), ts.STRING).func().ret();
        Assert.assertEquals(ts.STRING, ret);
    }

    @Test
    public void specify_type_args() {
        testInvoke();
    }

    @Test
    public void test_to_string() {
        Assert.assertEquals("(arg0: string): string", sig("(arg0: string) :string").toString());
        Assert.assertEquals("(arg0: string): void", sig("(arg0: string)").toString());
        Assert.assertEquals("(<T>: string, arg0: T): T", sig("(<T>: string, arg0: T): T").toString());
    }

    public void testInvoke() {
        FluentAPI testData = testDataFromMySection();
        List<String> codes = testData.codes();
        if (codes.isEmpty()) {
            throw new RuntimeException("no code found");
        }
        for (int i = 0; i < codes.size() - 1; i++) {
            String code = codes.get(i);
            ts.defineInterface(DexInterface.$(code));
        }
        FunctionSig sig = sig(codes.get(codes.size() - 1));
        if ("typeArgs".equals(testData.table().head.get(0))) {
            testData.assertByTable((typeArgs, posArgs) -> sig.invoke(
                    ResolvePosArgs.$(ts, stripQuote(typeArgs)),
                    ResolvePosArgs.$(ts, stripQuote(posArgs)),
                    null));
        } else {
            testData.assertByTable(posArgs -> sig.invoke(
                    Collections.emptyList(),
                    ResolvePosArgs.$(ts, stripQuote(posArgs)),
                    null));
        }
    }

    private FunctionSig sig(String src) {
        FunctionSig sig = new FunctionSig(ts, null, DexSig.$(src));
        sig.reparent(new FunctionType(ts, "", sig.params(), sig.ret()));
        return sig;
    }
}
