package com.dexscript.ast;

import org.junit.Assert;
import org.junit.Test;

public class DexInterfaceTest {

    @Test
    public void empty() {
        DexInterface inf = new DexInterface("interface Duck {}");
        Assert.assertEquals("interface Duck {}", inf.toString());
        Assert.assertEquals("Duck", inf.identifier().toString());
    }

    @Test
    public void global_interface() {
        DexInterface inf = new DexInterface("interface :: {}");
        Assert.assertEquals("interface :: {}", inf.toString());
        Assert.assertEquals("::", inf.identifier().toString());
    }

    @Test
    public void no_space_between_interface_keyword_and_identifier() {
        DexInterface inf = new DexInterface("interfaceDuck {}");
        Assert.assertEquals("<unmatched>interfaceDuck {}</unmatched>", inf.toString());
    }

    @Test
    public void no_space_between_identifier_and_left_brace() {
        DexInterface inf = new DexInterface("interface Duck{}");
        Assert.assertEquals("interface Duck{}", inf.toString());
    }

    @Test
    public void inf_method() {
        String src = "" +
                "interface Duck {\n" +
                "   Quack(): string\n" +
                "}";
        DexInterface inf = new DexInterface(src);
        inf.methods();
        Assert.assertEquals("{\n" +
                "   Quack(): string\n" +
                "}", inf.body().toString());
        Assert.assertEquals(1, inf.methods().size());
        Assert.assertEquals("Quack(): string", inf.methods().get(0).toString());
    }

    @Test
    public void inf_function() {
        String src = "" +
                "interface Duck {\n" +
                "   ::Quack(duck: Duck): string\n" +
                "}";
        DexInterface inf = new DexInterface(src);
        inf.functions();
        Assert.assertEquals("{\n" +
                "   ::Quack(duck: Duck): string\n" +
                "}", inf.body().toString());
        Assert.assertEquals(1, inf.functions().size());
        Assert.assertEquals("Quack(duck: Duck): string", inf.functions().get(0).toString());
    }

    @Test
    public void recover_invalid_inf_member_by_line_end() {
        String src = "" +
                "interface Duck {\n" +
                "   ??;" +
                "   Quack(): string\n" +
                "}";
        DexInterface inf = new DexInterface(src);
        inf.methods();
        Assert.assertEquals("{\n" +
                "   <error/>??;   Quack(): string\n" +
                "}", inf.body().toString());
        Assert.assertEquals(1, inf.methods().size());
        Assert.assertEquals("Quack(): string", inf.methods().get(0).toString());
    }

    @Test
    public void type_parameter() {
        String src = "" +
                "interface List {\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}";
        DexInterface inf = new DexInterface(src);
        inf.methods();
        Assert.assertEquals("{\n" +
                "   <T>: string\n" +
                "   Get__(index: int64): T\n" +
                "}", inf.body().toString());
        Assert.assertEquals(1, inf.typeParams().size());
        Assert.assertEquals("<T>: string", inf.typeParams().get(0).toString());
    }

    @Test
    public void two_type_parameters() {
        String src = "" +
                "interface AnotherInf {\n" +
                "   <E1>: interface{};\n" +
                "   <E2>: interface{};\n" +
                "   Get__(index: '0', arg: E1)\n" +
                "   Get__(index: '1', arg: E2)\n" +
                "}";

        DexInterface inf = new DexInterface(src);
        inf.methods();
        Assert.assertEquals("{\n" +
                "   <E1>: interface{};\n" +
                "   <E2>: interface{};\n" +
                "   Get__(index: '0', arg: E1)\n" +
                "   Get__(index: '1', arg: E2)\n" +
                "}", inf.body().toString());
        Assert.assertEquals("<E1>: interface{}", inf.typeParams().get(0).toString());
        Assert.assertEquals("<E2>: interface{}", inf.typeParams().get(1).toString());
        Assert.assertEquals("Get__(index: '0', arg: E1)", inf.methods().get(0).toString());
    }
}
