package com.dexscript.ast.type;

import com.dexscript.test.framework.TestFramework;
import org.junit.Assert;
import org.junit.Test;

import static com.dexscript.test.framework.TestFramework.testDataFrom;

public class DexInterfaceTypeTest {

    @Test
    public void empty_interface() {
        TestFramework.assertParsedAST(DexInterfaceType::$);
    }

    @Test
    public void one_function() {
        TestFramework.assertParsedAST(DexInterfaceType::$);
    }

    @Test
    public void one_method() {
        DexInterfaceType inf = DexInterfaceType.$("interface {\n" +
                "   Hello(msg: string): string\n" +
                "}");
        Assert.assertEquals(1, inf.methods().size());
    }

    @Test
    public void function_and_method() {
        DexInterfaceType inf = DexInterfaceType.$("interface {\n" +
                "   ::Hello(msg: string): string\n" +
                "   Hello(msg: string): string\n" +
                "}");
        Assert.assertEquals(1, inf.functions().size());
        Assert.assertEquals(1, inf.methods().size());
    }

    @Test
    public void missing_method() {
        DexInterfaceType inf = DexInterfaceType.$("interface {\n" +
                "   ?? Hello(msg: string): string\n" +
                "}");
        System.out.println("interface {\n" +
                "   <error/>?? Hello(msg: string): string\n" +
                "}");
        Assert.assertEquals("interface {\n" +
                "   <error/>?? Hello(msg: string): string\n" +
                "}", inf.toString());
        Assert.assertEquals(1, inf.methods().size());
    }
}
