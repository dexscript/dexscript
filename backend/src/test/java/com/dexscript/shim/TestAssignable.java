package com.dexscript.shim;

import com.dexscript.ast.DexInterface;
import com.dexscript.infer.ResolvePosArgs;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.transpile.OutTown;
import com.dexscript.type.DType;
import com.dexscript.type.IsAssignable;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;

import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public interface TestAssignable {

    static void $() {
        TestAssignable.$(new OutTown());
    }

    static void $(OutTown oTown) {
        FluentAPI testData = testDataFromMySection();
        TypeSystem ts = oTown.oShim().typeSystem();
        for (String code : testData.codes("dexscript")) {
            ts.defineInterface(DexInterface.$(code));
        }
        for (Row row : testData.table().body) {
            DType to = ResolvePosArgs.$(ts, stripQuote(row.get(1))).get(0);
            DType from = ResolvePosArgs.$(ts, stripQuote(row.get(2))).get(0);
            TestAssignable.$("true".equals(row.get(0)), to, from);
        }
    }

    static void $(boolean expected, DType to, DType from) {
        IsAssignable isAssignable = new IsAssignable(to, from);
        if (expected != isAssignable.result()) {
            isAssignable.dump();
            Assert.fail();
        }
    }
}
