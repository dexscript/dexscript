package com.dexscript.type;

import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.type.DexType;
import com.dexscript.infer.InferType;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SubstituteConstTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void empty() {
        SubstituteConst iterator = new SubstituteConst(new ArrayList<>());
        List<List<DType>> combinations = permute(iterator);
        Assert.assertEquals(1, combinations.size());
        Assert.assertEquals(new ArrayList<List<DType>>(), combinations.get(0));
    }

    @Test
    public void no_const() {
        List<List<DType>> combinations = permute("string");
        Assert.assertEquals(1, combinations.size());
        Assert.assertEquals(resolve("string"), combinations.get(0));
    }

    @Test
    public void one_bool_const() {
        List<List<DType>> combinations = permute("(const)true, string");
        Assert.assertEquals(2, combinations.size());
        Assert.assertEquals(resolve("true, string"), combinations.get(0));
        Assert.assertEquals(resolve("bool, string"), combinations.get(1));
    }

    @Test
    public void one_string_const() {
        List<List<DType>> combinations = permute("(const)'hello', string");
        Assert.assertEquals(2, combinations.size());
        Assert.assertEquals(resolve("'hello', string"), combinations.get(0));
        Assert.assertEquals(resolve("string, string"), combinations.get(1));
    }

    @Test
    public void one_integer_const() {
        List<List<DType>> combinations = permute("(const)100, string");
        Assert.assertEquals(5, combinations.size());
        Assert.assertEquals(resolve("100, string"), combinations.get(0));
        Assert.assertEquals(resolve("int64, string"), combinations.get(1));
        Assert.assertEquals(resolve("int32, string"), combinations.get(2));
        Assert.assertEquals(resolve("float64, string"), combinations.get(3));
        Assert.assertEquals(resolve("float32, string"), combinations.get(4));
    }

    @Test
    public void one_float_const() {
        List<List<DType>> combinations = permute("(const)100.0, string");
        Assert.assertEquals(2, combinations.size());
        Assert.assertEquals(resolve("float64, string"), combinations.get(0));
        Assert.assertEquals(resolve("float32, string"), combinations.get(1));
    }

    @Test
    public void two_bool_const() {
        List<List<DType>> combinations = permute("(const)true, (const)false");
        Assert.assertEquals(4, combinations.size());
        Assert.assertEquals(resolve("true, false"), combinations.get(0));
        Assert.assertEquals(resolve("bool, false"), combinations.get(1));
        Assert.assertEquals(resolve("true, bool"), combinations.get(2));
        Assert.assertEquals(resolve("bool, bool"), combinations.get(3));
    }

    @Test
    public void three_bool_const() {
        List<List<DType>> combinations = permute("(const)true, (const)false, (const)true");
        Assert.assertEquals(8, combinations.size());
        Assert.assertEquals(resolve("true, false, true"), combinations.get(0));
        Assert.assertEquals(resolve("bool, false, true"), combinations.get(1));
        Assert.assertEquals(resolve("true, bool, true"), combinations.get(2));
        Assert.assertEquals(resolve("bool, bool, true"), combinations.get(3));
        Assert.assertEquals(resolve("true, false, bool"), combinations.get(4));
        Assert.assertEquals(resolve("bool, false, bool"), combinations.get(5));
        Assert.assertEquals(resolve("true, bool, bool"), combinations.get(6));
        Assert.assertEquals(resolve("bool, bool, bool"), combinations.get(7));
    }

    private List<List<DType>> permute(String src) {
        List<DType> types = resolve(src);
        return permute(new SubstituteConst(types));
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

    @NotNull
    public List<List<DType>> permute(SubstituteConst iterator) {
        ArrayList<List<DType>> combinations = new ArrayList<>();
        while (iterator.hasNext()) {
            combinations.add(iterator.next());
        }
        return combinations;
    }
}
