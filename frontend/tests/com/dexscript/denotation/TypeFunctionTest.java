package com.dexscript.denotation;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class TypeFunctionTest {

    @Test
    public void assignable() {
        TypeFunction hello1 = new TypeFunction("hello", new ArrayList<>(), BuiltinTypes.STRING);
        TypeFunction hello2 = new TypeFunction("hello", new ArrayList<>(), BuiltinTypes.STRING);
        Assert.assertTrue(hello1.isAssignableFrom(hello2));
        Assert.assertTrue(hello2.isAssignableFrom(hello1));
    }

    @Test
    public void name_not_assignable() {
        Assert.assertFalse(new TypeFunction("hello", new ArrayList<>(), BuiltinTypes.STRING).isAssignableFrom(
                new TypeFunction("world", new ArrayList<>(), BuiltinTypes.STRING)));
    }

    @Test
    public void params_count_not_assignable() {
        Assert.assertFalse(new TypeFunction("hello", new ArrayList<>(), BuiltinTypes.STRING).isAssignableFrom(
                new TypeFunction("hello", new ArrayList<>() {{
                    add(BuiltinTypes.STRING);
                }}, BuiltinTypes.STRING)));
    }

    @Test
    public void params_not_assignable() {
        Assert.assertFalse(new TypeFunction("hello", new ArrayList<>() {{
            add(BuiltinTypes.VOID);
        }}, BuiltinTypes.STRING).isAssignableFrom(
                new TypeFunction("hello", new ArrayList<>() {{
                    add(BuiltinTypes.STRING);
                }}, BuiltinTypes.STRING)));
    }

    @Test
    public void ret_not_assignable() {
        Assert.assertFalse(new TypeFunction("hello", new ArrayList<>(), BuiltinTypes.STRING).isAssignableFrom(
                new TypeFunction("hello", new ArrayList<>(), BuiltinTypes.VOID)));
    }
}
