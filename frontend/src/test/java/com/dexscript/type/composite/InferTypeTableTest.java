package com.dexscript.type.composite;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.type.DexType;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.type.core.*;
import org.junit.Test;

import static com.dexscript.test.framework.AssertByTable.accessByPath;
import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class InferTypeTableTest {

    @Test
    public void interface_type_table() {
        testInferTypeTable();
    }

    private static void testInferTypeTable() {
        InterfaceType.init();
        FluentAPI testData = testDataFromMySection();
        TypeSystem ts = new TypeSystem();
        DexFile dexFile = DexFile.$(testData.code());
        for (Row row : testData.table().body) {
            TypeTable typeTable = InferTypeTable.$(ts, (DexElement) accessByPath(dexFile, row.get(0)));
            DType actual = typeTable.resolveType(DexPackage.DUMMY, row.get(1));
            DType expected = InferType.$(ts, DexType.$parse(stripQuote(row.get(2))));
            TestAssignable.$(true, actual, expected);
        }
    }
}
