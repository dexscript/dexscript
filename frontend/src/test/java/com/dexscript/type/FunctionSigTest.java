package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.type.DexType;
import com.dexscript.test.framework.FluentAPI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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
        DType ret = sig.invoke(Collections.emptyList(), resolve(), ts.ANY, ts.STRING).func().ret();
        Assert.assertEquals(ts.STRING, ret);
    }

    @Test
    public void specify_type_args() {
        testInvoke();
    }

    @Test
    public void specify_context_arg() {
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
        Text src = new Text(typeDefs);
        List<DType> args = new ArrayList<>();
        while(true) {
            DexType arg = DexType.parse(src);
            arg.attach(DexPackage.DUMMY);
            if (!arg.matched()) {
                throw new RuntimeException("unable to parse invocation args: " + src);
            }
            args.add(ResolveType.$(ts, null, arg));
            src = src.slice(arg.end());
            int i = src.begin;
            for (; i < src.end; i++) {
                if (Blank.$(src.bytes[i]) || src.bytes[i] == ',') {
                    continue;
                }
                src = src.slice(i);
                break;
            }
            if (i == src.end) {
                break;
            }
        }
        return args;
    }

    private List<DType> resolve(String... typeDefs) {
        return ResolveType.resolveTypes(ts, typeDefs);
    }
}
