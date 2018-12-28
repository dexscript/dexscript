package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.type.DexType;
import com.dexscript.infer.InferType;
import com.dexscript.test.framework.FluentAPI;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class FunctionTableTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void only_match_one() {
        testDispatch();
    }

    @Test
    public void need_runtime_check() {
        testDispatch();
    }

    @Test
    public void if_matched_then_following_candidates_will_be_skipped() {
        testDispatch();
    }

    @Test
    public void if_not_match_then_candidate_will_be_ignored() {
        testDispatch();
    }

    @Test
    public void invoke_interface_without_impl() {
        testDispatch();
    }

    @Test
    public void invoke_interface_with_impl() {
        ts.defineInterface(DexInterface.$("" +
                "interface Hello {" +
                "   SayHello(msg: string)\n" +
                "}"));
        func("SayHello(self: int64, arg0: string)");
        Dispatched dispatched = invoke("SayHello", null, "Hello,string", true);
        Assert.assertEquals(1, dispatched.candidates.size());
        Assert.assertEquals(1, dispatched.ignoreds.size());
    }

    @Test
    public void ignore_candidate_wider_than_the_match() {
        ts.defineInterface(DexInterface.$("" +
                "interface Hello {" +
                "   SayHello(msg: string)\n" +
                "}"));
        func("SayHello(self: int64, arg0: interface{})");
        Dispatched dispatched = invoke("SayHello", null, "Hello,string", true);
        Assert.assertEquals(0, dispatched.candidates.size());
        Assert.assertEquals(2, dispatched.ignoreds.size());
    }

    @Test
    public void widen_const_type() {
        func("Hello(arg0: int32)");
        Dispatched dispatched = invoke("Hello", "(const)100");
        Assert.assertEquals(1, dispatched.candidates.size());
        Assert.assertEquals(ts.INT32, dispatched.args.get(0));
    }

    @Test
    public void call_with_named_arg() {
        func("Hello(a: int32)");
        Dispatched dispatched = invoke("Hello", null,
                "a", "int32");
        Assert.assertEquals(1, dispatched.candidates.size());
        Assert.assertEquals(ts.INT32, dispatched.args.get(0));
        Assert.assertEquals(0, dispatched.namedArgsMapping[0]);
    }

    @Test
    public void call_with_two_named_args() {
        func("Hello(a: int32, b: int64)");
        Dispatched dispatched = invoke("Hello", null,
                "b", "int64",
                "a", "int32");
        Assert.assertEquals(1, dispatched.candidates.size());
        Assert.assertEquals(ts.INT32, dispatched.args.get(0));
        Assert.assertEquals(ts.INT64, dispatched.args.get(1));
        Assert.assertEquals(1, dispatched.namedArgsMapping[0]);
        Assert.assertEquals(0, dispatched.namedArgsMapping[1]);
    }

    private void testDispatch() {
        FluentAPI testData = testDataFromMySection();
        for (String code : testData.codes()) {
            if (code.startsWith("interface")) {
                ts.defineInterface(DexInterface.$(code));
            } else {
                func(code);
            }
        }
        testData.assertByTable((funcName, posArgs) -> ts.dispatch(new Invocation(
                funcName, Collections.emptyList(),
                ResolvePosArgs.$(ts, stripQuote(posArgs)),
                Collections.emptyList(), ts.ANY, null)));
    }

    private FunctionType func(String actorSrc) {
        DexActor actor = DexActor.$("function " + actorSrc);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType funcType = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
        funcType.implProvider(expandedFunc -> new Object());
        return funcType;
    }

    private Dispatched invoke(String funcName, String argsStr, Object... namedArgs) {
        return _invoke(funcName, null, argsStr, namedArgs, false);
    }

    private Dispatched _invoke(String funcName, String typeArgsStr, String argsStr, Object[] namedArgObjs, boolean requiresImpl) {
        List<DType> typeArgs = new ArrayList<>();
        if (typeArgsStr != null) {
            typeArgs = resolve(typeArgsStr);
        }
        List<DType> posArgs = new ArrayList<>();
        if (argsStr != null) {
            posArgs = resolve(argsStr);
        }
        List<NamedArg> namedArgs = new ArrayList<>();
        for (int i = 0; i < namedArgObjs.length; i += 2) {
            String name = (String) namedArgObjs[i];
            DType type = resolve((String) namedArgObjs[i + 1]).get(0);
            namedArgs.add(new NamedArg(name, type));
        }
        return ts.dispatch(new Invocation(funcName, typeArgs, posArgs, namedArgs, ts.ANY, null).requireImpl(requiresImpl));
    }

    @NotNull
    private List<DType> resolve(String src) {
        List<DType> types = new ArrayList<>();
        for (String typeSrc : src.split(",")) {
            typeSrc = typeSrc.trim();
            if (typeSrc.startsWith("(const)")) {
                DexExpr expr = DexExpr.$parse(typeSrc.substring("(const)".length()));
                types.add(InferType.$(ts, expr));
            } else {
                types.add(ResolveType.$(ts, null, DexType.$parse(typeSrc)));
            }
        }
        return types;
    }
}
