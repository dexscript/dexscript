package com.dexscript.type;

import com.dexscript.infer.ResolveNamedArgs;
import com.dexscript.infer.ResolvePosArgs;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.test.framework.Table;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class SubstituteConstTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void empty() {
        SubstituteConst iterator = new SubstituteConst(new ArrayList<>(), new ArrayList<>());
        List<SubstituteConst.Combination> combinations = permute(iterator);
        Assert.assertEquals(1, combinations.size());
        Assert.assertTrue(combinations.get(0).posArgs.isEmpty());
        Assert.assertTrue(combinations.get(0).namedArgs.isEmpty());
    }

    @Test
    public void no_const() {
        testSubstituteConst();
    }

    @Test
    public void one_bool_const() {
        testSubstituteConst();
    }

    @Test
    public void one_string_const() {
        testSubstituteConst();
    }

    @Test
    public void one_integer_const() {
        testSubstituteConst();
    }

    @Test
    public void one_float_const() {
        testSubstituteConst();
    }

    @Test
    public void one_pos_const_and_one_named_const() {
        testSubstituteConst();
    }

    @Test
    public void two_bool_const() {
        testSubstituteConst();
    }

    @Test
    public void three_bool_const() {
        testSubstituteConst();
    }

    @NotNull
    public List<SubstituteConst.Combination> permute(SubstituteConst iterator) {
        ArrayList<SubstituteConst.Combination> combinations = new ArrayList<>();
        while (iterator.hasNext()) {
            combinations.add(iterator.next());
        }
        return combinations;
    }

    private void testSubstituteConst() {
        FluentAPI testData = testDataFromMySection();
        List<String> codes = testData.codes();
        if (codes.isEmpty()) {
            throw new RuntimeException("code not found");
        }
        List<NamedArg> namedArgs = Collections.emptyList();
        if (codes.size() >= 2) {
            namedArgs = ResolveNamedArgs.$(ts, codes.get(1));
        }
        SubstituteConst iterator = new SubstituteConst(
                ResolvePosArgs.$(ts, codes.get(0)),
                namedArgs);
        List<SubstituteConst.Combination> combinations = permute(iterator);
        Table table = testData.table();
        Assert.assertEquals(table.body.size(), combinations.size());
        for (int i = 0; i < table.body.size(); i++) {
            Row row = table.body.get(i);
            SubstituteConst.Combination combination = combinations.get(i);
            Assert.assertEquals(ResolvePosArgs.$(ts, stripQuote(row.get(0))), combination.posArgs);
            if (row.size() >= 2) {
                Assert.assertEquals(ResolveNamedArgs.$(ts, stripQuote(row.get(1))), combination.namedArgs);
            } else {
                Assert.assertEquals(0, combination.namedArgs.size());
            }
        }
    }
}
