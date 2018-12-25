package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexInterfaceTypeTest {

    @Test
    public void empty_interface() {
        DexInterfaceType inf = new DexInterfaceType("interface {}");
        Assert.assertEquals("interface {}", inf.toString());
        Assert.assertEquals(0, inf.methods().size());
        Assert.assertEquals(0, inf.functions().size());
    }

    @Test
    public void one_function() {
        System.out.println("interface {\n" +
                "   ::Hello(msg: string): string\n" +
                "}");
        DexInterfaceType inf = new DexInterfaceType("interface {\n" +
                "   ::Hello(msg: string): string\n" +
                "}");
        inf.functions().get(0).identifier()
        Assert.assertEquals(1, inf.functions().size());
    }

    @Test
    public void one_method() {
        DexInterfaceType inf = new DexInterfaceType("interface {\n" +
                "   Hello(msg: string): string\n" +
                "}");
        Assert.assertEquals(1, inf.methods().size());
    }

    @Test
    public void function_and_method() {
        DexInterfaceType inf = new DexInterfaceType("interface {\n" +
                "   ::Hello(msg: string): string\n" +
                "   Hello(msg: string): string\n" +
                "}");
        Assert.assertEquals(1, inf.functions().size());
        Assert.assertEquals(1, inf.methods().size());
    }

    @Test
    public void missing_method() {
        DexInterfaceType inf = new DexInterfaceType("interface {\n" +
                "   ?? Hello(msg: string): string\n" +
                "}");
        Assert.assertEquals("interface {\n" +
                "   <error/>?? Hello(msg: string): string\n" +
                "}", inf.toString());
        Assert.assertEquals(1, inf.methods().size());
    }
}
