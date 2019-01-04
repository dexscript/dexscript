package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.type.DexType;
import com.dexscript.infer.InferType;
import com.dexscript.infer.ResolveNamedArgs;
import com.dexscript.infer.ResolvePosArgs;
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
        testDispatch();
    }

    @Test
    public void impl_should_override_interface() {
        testDispatch();
    }

    @Test
    public void widen_const_type() {
        testDispatch();
    }

    @Test
    public void call_with_named_arg() {
        testDispatch();
    }

    @Test
    public void call_with_two_named_args() {
        testDispatch();
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
        if ("namedArgs".equals(testData.table().head.get(2))) {
            testData.assertByTable((funcName, posArgs, namedArgs) -> ts.dispatch(new Invocation(
                    funcName, Collections.emptyList(),
                    ResolvePosArgs.$(ts, stripQuote(posArgs)),
                    ResolveNamedArgs.$(ts, stripQuote(namedArgs)), null).requireImpl(true)));
        } else {
            testData.assertByTable((funcName, posArgs) -> ts.dispatch(new Invocation(
                    funcName, Collections.emptyList(),
                    ResolvePosArgs.$(ts, stripQuote(posArgs)),
                    Collections.emptyList(), null).requireImpl(true)));
        }
    }

    private FunctionType func(String actorSrc) {
        DexActor actor = DexActor.$("function " + actorSrc);
        FunctionSig sig = new FunctionSig(ts, null, actor.sig());
        FunctionType funcType = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret());
        funcType.implProvider(expandedFunc -> new Object());
        return funcType;
    }
}
