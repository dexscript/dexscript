package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.test.framework.Table;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

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
    public void context_not_assignable() {
        FunctionType func1 = func("hello()");
        FunctionSig sig = new FunctionSig(ts, Collections.emptyList(), ts.STRING, ts.VOID);
        FunctionType func2 = new FunctionType(ts, "hello", Collections.emptyList(), ts.VOID, sig);
        TestAssignable.$(false, func1, func2);
        TestAssignable.$(true, func2, func1);
    }

    @Test
    public void param_is_sub_type() {
        FunctionType hello1 = func("hello(arg0: string)");
        FunctionType hello2 = func("hello(arg0: 'example')");
        TestAssignable.$(false, hello1, hello2);
        TestAssignable.$(true, hello2, hello1);
    }

    @Test
    public void ret_is_sub_type() {
        FunctionType hello1 = func("hello(): string");
        FunctionType hello2 = func("hello(): 'example'");
        TestAssignable.$(true, hello1, hello2);
        TestAssignable.$(false, hello2, hello1);
    }

    @Test
    public void test_to_string() {
        Assert.assertEquals("Hello(arg0: string): void", func("Hello(arg0: string): void").toString());
    }

    private FunctionType func(String src) {
        DexActor actor = DexActor.$("function " + src);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        return new FunctionType(ts, actor.functionName(), sig.params(), sig.ret(), sig);
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
