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
        Assert.assertEquals(1, inf.members().size());
        Assert.assertEquals("Quack(): string", inf.members().get(0).toString());
    }
}
