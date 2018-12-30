package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.infer.ResolvePosArgs;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.test.framework.Table;
import org.junit.Assert;

import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public interface TestAssignable {

    static void $(boolean expected, DType to, DType from) {
        IsAssignable isAssignable = new IsAssignable(to, from);
        if (expected != isAssignable.result()) {
            isAssignable.dump();
            Assert.fail();
        }
    }

    static void $() {
        TypeSystem ts = new TypeSystem();
        FluentAPI testData = testDataFromMySection();
        for (String code : testData.codes()) {
            if (code.startsWith("interface")) {
                ts.defineInterface(DexInterface.$(code));
            } else {
                func(ts, code);
            }
        }
        Table table = testData.table();
        for (Row row : table.body) {
            DType to = ResolvePosArgs.$(ts, stripQuote(row.get(1))).get(0);
            DType from = ResolvePosArgs.$(ts, stripQuote(row.get(2))).get(0);
            TestAssignable.$("true".equals(row.get(0)), to, from);
        }
    }

    static void func(TypeSystem ts, String src) {
        DexActor actor = DexActor.$("function " + src);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        FunctionType function = new FunctionType(ts, actor.functionName(), sig.params(), sig.ret());
        function.implProvider(expandedFunc -> new Object());
    }
}
