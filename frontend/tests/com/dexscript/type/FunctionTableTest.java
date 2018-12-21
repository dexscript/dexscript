package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.type.DexType;
import com.dexscript.infer.InferType;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
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
    public void widen_const_type() {
        func("Hello(arg0: int32)");
        Invoked invoked = invoke("Hello", "(const)100");
        Assert.assertEquals(1, invoked.candidates.size());
        Assert.assertEquals(ts.INT32, invoked.args.get(0));
    }

    private FunctionType func(String actorSrc) {
        DexActor actor = new DexActor("function " + actorSrc);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType funcType = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
        return funcType;
    }

    public Invoked invoke(String funcName, String argsStr) {
        return invoke(funcName, null, argsStr);
    }

    public Invoked invoke(String funcName, String typeArgsStr, String argsStr) {
        List<DType> typeArgs = Collections.emptyList();
        if (typeArgsStr != null) {
            typeArgs = resolve(typeArgsStr);
        }
        List<DType> args = resolve(argsStr);
        return ts.invoke(new Invocation(funcName, typeArgs, args, null));
    }

    @NotNull
    private List<DType> resolve(String src) {
        List<DType> types = new ArrayList<>();
        for (String typeSrc : src.split(",")) {
            typeSrc = typeSrc.trim();
            if (typeSrc.startsWith("(const)")) {
                DexExpr expr = DexExpr.parse(typeSrc.substring("(const)".length()));
                types.add(InferType.$(ts, expr));
            } else {
                types.add(ResolveType.$(ts, null, DexType.parse(typeSrc)));
            }
        }
        return types;
    }
}
