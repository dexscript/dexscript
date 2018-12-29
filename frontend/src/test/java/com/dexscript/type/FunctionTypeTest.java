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
    public void context_not_assignable() {
        DexPackage pkg1 = new DexPackage("pkg1");
        FunctionType func1 = func(pkg1, "hello()");
        defineInterface(pkg1, "" +
                "interface $ {\n" +
                "   Some()\n" +
                "}");
        DexPackage pkg2 = new DexPackage("pkg2");
        FunctionType func2 = func(pkg2, "hello()");
        defineInterface(pkg2, "" +
                "interface $ {\n" +
                "   Another()\n" +
                "}");
        TestAssignable.$(false, func1, func2);
        TestAssignable.$(false, func2, func1);
    }

    @Test
    public void context_is_sub_type() {
        DexPackage pkg1 = new DexPackage("pkg1");
        FunctionType func1 = func(pkg1, "hello()");
        defineInterface(pkg1, "" +
                "interface $ {\n" +
                "   Some()\n" +
                "}");
        DexPackage pkg2 = new DexPackage("pkg2");
        FunctionType func2 = func(pkg2, "hello()");
        defineInterface(pkg2, "" +
                "interface $ {\n" +
                "   Some()\n" +
                "   Another()\n" +
                "}");
        TestAssignable.$(false, func1, func2);
        TestAssignable.$(true, func2, func1);
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

    private FunctionType func(String src) {
        return func(DexPackage.DUMMY, src);
    }

    private FunctionType func(DexPackage pkg, String src) {
        DexActor actor = new DexActor(new Text("function " + src));
        actor.attach(pkg);
        FunctionSig sig = new FunctionSig(ts, actor.sig());
        return new FunctionType(ts, pkg, actor.functionName(), sig.params(), sig.ret());
    }

    private void defineInterface(DexPackage pkg1, String src) {
        DexInterface globalSpi = new DexInterface(new Text(src));
        globalSpi.attach(pkg1);
        ts.defineInterface(globalSpi);
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
