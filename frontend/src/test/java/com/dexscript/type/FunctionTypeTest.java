package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.Text;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.test.framework.Table;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class FunctionTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void same_function_is_assignable() {
        testAssignable();
    }

    @Test
    public void name_not_assignable() {
        testAssignable();
    }

    @Test
    public void params_count_not_assignable() {
        testAssignable();
    }

    @Test
    public void param_name_not_assignable() {
        testAssignable();
    }

    @Test
    public void param_type_not_assignable() {
        testAssignable();
    }

    @Test
    public void ret_not_assignable() {
        testAssignable();
    }

    @Test
    public void param_is_sub_type() {
        testAssignable();
    }

    @Test
    public void ret_is_sub_type() {
        testAssignable();
    }

    @Test
    public void test_to_string() {
        Assert.assertEquals("Hello(arg0: string): void", func("Hello(arg0: string): void").toString());
    }

    @Test
    public void one_type_param() {
        testAssignable();
    }

    @Test
    public void argument_referenced_type_param() {
        testAssignable();
    }

    private FunctionType func(String src) {
        return func(DexPackage.DUMMY, src);
    }

    private FunctionType func(DexPackage pkg, String src) {
        DexActor actor = new DexActor(new Text("function " + src));
        actor.attach(pkg);
        return new FunctionType(ts, actor.functionName(), null, actor.sig());
    }

    private void testAssignable() {
        FluentAPI testData = testDataFromMySection();
        Table table = testData.table();
        for (Row row : table.body) {
            boolean isAssignable = "true".equals(row.get(0));
            String toSrc = stripQuote(row.get(1));
            DType to = toSrc.contains(":") ? func(toSrc) : ResolveType.$(ts, toSrc);
            String fromSrc = stripQuote(row.get(2));
            DType from = fromSrc.contains(":") ? func(fromSrc) : ResolveType.$(ts, fromSrc);
            TestAssignable.$(isAssignable, to, from);
        }
    }
}
