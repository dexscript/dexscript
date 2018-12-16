package com.dexscript.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void assignable() {
        FunctionType hello1 = new FunctionType(ts, "hello", new ArrayList<>(), ts.STRING);
        FunctionType hello2 = new FunctionType(ts, "hello", new ArrayList<>(), ts.STRING);
        Assert.assertTrue(hello1.isAssignableFrom(hello2));
        Assert.assertTrue(hello2.isAssignableFrom(hello1));
    }

    @Test
    public void name_not_assignable() {
        Assert.assertFalse(new FunctionType(ts, "hello", new ArrayList<>(), ts.STRING).isAssignableFrom(
                new FunctionType(ts, "world", new ArrayList<>(), ts.STRING)));
    }

    @Test
    public void params_count_not_assignable() {
        Assert.assertFalse(new FunctionType(ts, "hello", new ArrayList<>(), ts.STRING).isAssignableFrom(
                new FunctionType(ts, "hello", new ArrayList<DType>() {{
                    add(ts.STRING);
                }}, ts.STRING)));
    }

    @Test
    public void params_not_assignable() {
        Assert.assertFalse(new FunctionType(ts, "hello", new ArrayList<DType>() {{
            add(ts.VOID);
        }}, ts.STRING).isAssignableFrom(
                new FunctionType(ts, "hello", new ArrayList<DType>() {{
                    add(ts.STRING);
                }}, ts.STRING)));
    }

    @Test
    public void ret_not_assignable() {
        Assert.assertFalse(new FunctionType(ts, "hello", new ArrayList<>(), ts.STRING).isAssignableFrom(
                new FunctionType(ts, "hello", new ArrayList<>(), ts.VOID)));
    }

    @Test
    public void param_is_sub_type() {
        FunctionType hello1 = new FunctionType(ts, "hello", new ArrayList<DType>() {{
            add(ts.STRING);
        }}, ts.VOID);
        FunctionType hello2 = new FunctionType(ts, "hello", new ArrayList<DType>() {{
            add(new StringLiteralType(ts, "example"));
        }}, ts.VOID);
        Assert.assertFalse(hello1.isAssignableFrom(hello2));
        Assert.assertTrue(hello2.isAssignableFrom(hello1));
    }

    @Test
    public void ret_is_sub_type() {
        FunctionType hello1 = new FunctionType(ts, "hello", new ArrayList<>(), ts.STRING);
        FunctionType hello2 = new FunctionType(ts, "hello", new ArrayList<>(), new StringLiteralType(ts, "example"));
        Assert.assertTrue(hello1.isAssignableFrom(hello2));
        Assert.assertFalse(hello2.isAssignableFrom(hello1));
    }

    @Test
    public void test_to_string() {
        FunctionType func = new FunctionType(ts, "Hello", Arrays.asList(ts.STRING), ts.VOID);
        Assert.assertEquals("Hello(string): void", func.toString());
    }
}
