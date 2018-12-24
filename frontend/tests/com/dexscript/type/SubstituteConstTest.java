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
        SubstituteConst iterator = new SubstituteConst(new ArrayList<>(), new ArrayList<>());
        List<SubstituteConst.Combination> combinations = permute(iterator);
        Assert.assertEquals(1, combinations.size());
        Assert.assertTrue(combinations.get(0).posArgs.isEmpty());
        Assert.assertTrue(combinations.get(0).namedArgs.isEmpty());
    }

    @Test
    public void no_const() {
        List<SubstituteConst.Combination> combinations = permute("string", "a", "int64");
        Assert.assertEquals(1, combinations.size());
        Assert.assertEquals(resolve("string"), combinations.get(0).posArgs);
        Assert.assertEquals(resolve("a", "int64"), combinations.get(0).namedArgs);
    }

    @Test
    public void one_bool_const() {
        List<SubstituteConst.Combination> combinations = permute("(const)true, string");
        Assert.assertEquals(2, combinations.size());
        Assert.assertEquals(resolve("true, string"), combinations.get(0).posArgs);
        Assert.assertEquals(resolve("bool, string"), combinations.get(1).posArgs);
    }

    @Test
    public void one_string_const() {
        List<SubstituteConst.Combination> combinations = permute("(const)'hello', string");
        Assert.assertEquals(2, combinations.size());
        Assert.assertEquals(resolve("'hello', string"), combinations.get(0).posArgs);
        Assert.assertEquals(resolve("string, string"), combinations.get(1).posArgs);
    }

    @Test
    public void one_integer_const() {
        List<SubstituteConst.Combination> combinations = permute("(const)100, string");
        Assert.assertEquals(5, combinations.size());
        Assert.assertEquals(resolve("100, string"), combinations.get(0).posArgs);
        Assert.assertEquals(resolve("int64, string"), combinations.get(1).posArgs);
        Assert.assertEquals(resolve("int32, string"), combinations.get(2).posArgs);
        Assert.assertEquals(resolve("float64, string"), combinations.get(3).posArgs);
        Assert.assertEquals(resolve("float32, string"), combinations.get(4).posArgs);
    }

    @Test
    public void one_float_const() {
        List<SubstituteConst.Combination> combinations = permute("(const)100.0, string");
        Assert.assertEquals(2, combinations.size());
        Assert.assertEquals(resolve("float64, string"), combinations.get(0).posArgs);
        Assert.assertEquals(resolve("float32, string"), combinations.get(1).posArgs);
    }

    @Test
    public void one_pos_const_and_one_named_const() {
        List<SubstituteConst.Combination> combinations = permute("(const)true", "a", "(const)false");
        Assert.assertEquals(4, combinations.size());
        Assert.assertEquals(resolve("true"), combinations.get(0).posArgs);
        Assert.assertEquals(resolve("a", "false"), combinations.get(0).namedArgs);
        Assert.assertEquals(resolve("bool"), combinations.get(1).posArgs);
        Assert.assertEquals(resolve("a", "false"), combinations.get(1).namedArgs);
        Assert.assertEquals(resolve("true"), combinations.get(2).posArgs);
        Assert.assertEquals(resolve("a", "bool"), combinations.get(2).namedArgs);
        Assert.assertEquals(resolve("bool"), combinations.get(3).posArgs);
        Assert.assertEquals(resolve("a", "bool"), combinations.get(3).namedArgs);
    }

    @Test
    public void two_bool_const() {
        List<SubstituteConst.Combination> combinations = permute("(const)true, (const)false");
        Assert.assertEquals(4, combinations.size());
        Assert.assertEquals(resolve("true, false"), combinations.get(0).posArgs);
        Assert.assertEquals(resolve("bool, false"), combinations.get(1).posArgs);
        Assert.assertEquals(resolve("true, bool"), combinations.get(2).posArgs);
        Assert.assertEquals(resolve("bool, bool"), combinations.get(3).posArgs);
    }

    @Test
    public void three_bool_const() {
        List<SubstituteConst.Combination> combinations = permute("(const)true, (const)false, (const)true");
        Assert.assertEquals(8, combinations.size());
        Assert.assertEquals(resolve("true, false, true"), combinations.get(0).posArgs);
        Assert.assertEquals(resolve("bool, false, true"), combinations.get(1).posArgs);
        Assert.assertEquals(resolve("true, bool, true"), combinations.get(2).posArgs);
        Assert.assertEquals(resolve("bool, bool, true"), combinations.get(3).posArgs);
        Assert.assertEquals(resolve("true, false, bool"), combinations.get(4).posArgs);
        Assert.assertEquals(resolve("bool, false, bool"), combinations.get(5).posArgs);
        Assert.assertEquals(resolve("true, bool, bool"), combinations.get(6).posArgs);
        Assert.assertEquals(resolve("bool, bool, bool"), combinations.get(7).posArgs);
    }

    private List<SubstituteConst.Combination> permute(String src, Object ...namedArgObjs) {
        List<DType> posArgs = resolve(src);
        List<NamedArg> namedArgs = resolve(namedArgObjs);
        return permute(new SubstituteConst(posArgs, namedArgs));
    }


    private List<NamedArg> resolve(Object ...namedArgObjs) {
        List<NamedArg> namedArgs = new ArrayList<>();
        for (int i = 0; i < namedArgObjs.length; i+=2) {
            String name = (String) namedArgObjs[i];
            DType type = resolve((String) namedArgObjs[i+1]).get(0);
            namedArgs.add(new NamedArg(name, type));
        }
        return namedArgs;
    }

    @NotNull
    private List<DType> resolve(String src) {
        List<DType> types = new ArrayList<>();
        for (String typeSrc : src.split(",")) {
            typeSrc = typeSrc.trim();
            if (typeSrc.startsWith("(const)")) {
                DexExpr expr = DexExpr.$parse(typeSrc.substring("(const)".length()));
                types.add(InferType.$(ts, expr));
            } else {
                types.add(ResolveType.$(ts, null, DexType.$parse(typeSrc)));
            }
        }
        return types;
    }

    @NotNull
    public List<SubstituteConst.Combination> permute(SubstituteConst iterator) {
        ArrayList<SubstituteConst.Combination> combinations = new ArrayList<>();
        while (iterator.hasNext()) {
            combinations.add(iterator.next());
        }
        return combinations;
    }
}
