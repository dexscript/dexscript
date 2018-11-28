package com.dexscript.resolve;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexReference;
import org.junit.Assert;
import org.junit.Test;

public class InterfaceTypeTest {

    @Test
    public void define_interface_type() {
        Resolve resolve = new Resolve();
        resolve.define(new DexInterface("" +
                "interface Duck {\n" +
                "  ::Quack(duck: Duck): string\n" +
                "}"));
        Denotation.InterfaceType inf = (Denotation.InterfaceType) resolve.resolveType(new DexReference("Duck"));
        Assert.assertEquals(1, inf.members().size());
        Assert.assertEquals("String", inf.members().get(0).ret().javaClassName());
    }
}
