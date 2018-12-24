package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.type.DexType;
import com.dexscript.infer.InferType;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FunctionTableTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void match_one() {
        FunctionType func1 = func("Hello(arg0: string)");
        FunctionType func2 = func("Hello(arg0: int64)");
        Invoked invoked = invoke("Hello", "string");
        Assert.assertEquals(1, invoked.candidates.size());
        Assert.assertEquals(func1, invoked.candidates.get(0).function());
    }

    @Test
    public void match_two() {
        func("Hello(arg0: 'a')");
        func("Hello(arg0: string)");
        Invoked invoked = invoke("Hello", "string");
        Assert.assertEquals(2, invoked.candidates.size());
    }

    @Test
    public void if_matched_then_following_candidates_will_be_skipped() {
        func("Hello(arg0: string)");
        func("Hello(arg0: 'a')");
        Invoked invoked = invoke("Hello", "string");
        Assert.assertEquals(1, invoked.candidates.size());
        Assert.assertEquals(1, invoked.skippeds.size());
    }

    @Test
    public void if_not_match_then_candidate_will_be_ignored() {
        func("Hello(arg0: 'a')"); // candidate, but not match
        Invoked invoked = invoke("Hello", "string");
        Assert.assertEquals(0, invoked.candidates.size());
        Assert.assertEquals(1, invoked.ignoreds.size());
    }

    @Test
    public void invoke_interface_without_impl() {
        ts.defineInterface(DexInterface.$("" +
                "interface Hello {" +
                "   SayHello(msg: string)\n" +
                "}"));
        Invoked invoked = invoke("SayHello", null, "Hello,string", true);
        Assert.assertEquals(0, invoked.candidates.size());
        Assert.assertEquals(1, invoked.ignoreds.size());
    }

    @Test
    public void invoke_interface_with_impl() {
        ts.defineInterface(DexInterface.$("" +
                "interface Hello {" +
                "   SayHello(msg: string)\n" +
                "}"));
        func("SayHello(self: int64, arg0: string)");
        Invoked invoked = invoke("SayHello", null, "Hello,string", true);
        Assert.assertEquals(1, invoked.candidates.size());
        Assert.assertEquals(1, invoked.ignoreds.size());
    }

    @Test
    public void ignore_candidate_wider_than_the_match() {
        ts.defineInterface(DexInterface.$("" +
                "interface Hello {" +
                "   SayHello(msg: string)\n" +
                "}"));
        func("SayHello(self: int64, arg0: interface{})");
        Invoked invoked = invoke("SayHello", null, "Hello,string", true);
        Assert.assertEquals(0, invoked.candidates.size());
        Assert.assertEquals(2, invoked.ignoreds.size());
    }

    @Test
    public void widen_const_type() {
        func("Hello(arg0: int32)");
        Invoked invoked = invoke("Hello", "(const)100");
        Assert.assertEquals(1, invoked.candidates.size());
        Assert.assertEquals(ts.INT32, invoked.args.get(0));
    }

    @Test
    public void call_with_named_arg() {
        func("Hello(a: int32)");
        Invoked invoked = invoke("Hello", null,
                "a", "int32");
        Assert.assertEquals(1, invoked.candidates.size());
        Assert.assertEquals(ts.INT32, invoked.args.get(0));
        Assert.assertEquals(0, invoked.namedArgsMapping[0]);
    }

    @Test
    public void call_with_two_named_args() {
        func("Hello(a: int32, b: int64)");
        Invoked invoked = invoke("Hello", null,
                "b", "int64",
                "a", "int32");
        Assert.assertEquals(1, invoked.candidates.size());
        Assert.assertEquals(ts.INT32, invoked.args.get(0));
        Assert.assertEquals(ts.INT64, invoked.args.get(1));
        Assert.assertEquals(1, invoked.namedArgsMapping[0]);
        Assert.assertEquals(0, invoked.namedArgsMapping[1]);
    }

    private FunctionType func(String actorSrc) {
        DexActor actor = DexActor.$("function " + actorSrc);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType funcType = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
        funcType.implProvider(expandedFunc -> new Object());
        return funcType;
    }

    private Invoked invoke(String funcName, String argsStr, Object... namedArgs) {
        return _invoke(funcName, null, argsStr, namedArgs, false);
    }

    private Invoked _invoke(String funcName, String typeArgsStr, String argsStr, Object[] namedArgObjs, boolean requiresImpl) {
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
        return ts.invoke(new Invocation(funcName, typeArgs, posArgs, namedArgs, ts.ANY, null).requireImpl(requiresImpl));
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
                types.add(ResolveType.$(ts, null, DexType.parse(typeSrc)));
            }
        }
        return types;
    }
}
