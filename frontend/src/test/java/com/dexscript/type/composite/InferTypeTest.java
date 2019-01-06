package com.dexscript.type.composite;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexType;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.InferType;
import com.dexscript.type.core.TestAssignable;
import com.dexscript.type.core.TypeSystem;
import org.junit.Test;

import static com.dexscript.test.framework.AssertByTable.accessByPath;
import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class InferTypeTest {

    @Test
    public void interface_method_param() {
        testInferType();
    }

    private static void testInferType() {
        InterfaceType.init();
        ActorType.init();
        FluentAPI testData = testDataFromMySection();
        TypeSystem ts = new TypeSystem();
        DexFile dexFile = DexFile.$(testData.code());
        for (Row row : testData.table().body) {
            DType actual = InferType.$(ts, (DexElement) accessByPath(dexFile, row.get(0)));
            DType expected = InferType.$(ts, DexType.$parse(stripQuote(row.get(1))));
            TestAssignable.$(true, actual, expected);
        }
    }
}
