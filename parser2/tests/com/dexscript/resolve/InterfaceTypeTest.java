package com.dexscript.resolve;

import com.dexscript.ast.DexInterface;
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
        Denotation.InterfaceType inf = (Denotation.InterfaceType) resolve.resolveType("Duck");
        Assert.assertEquals(1, inf.members().size());
        Assert.assertEquals("String", inf.members().get(0).ret().javaClassName());
    }

    @Test
    public void test_compatibility() {
        Resolve resolve = new Resolve();
        resolve.define(new DexInterface("" +
                "interface Duck1 {\n" +
                "  ::Quack(duck: Duck1): string\n" +
                "}"));
        resolve.define(new DexInterface("" +
                "interface Duck2 {\n" +
                "  ::Quack(duck: Duck2): string\n" +
                "}"));
        Denotation.InterfaceType duck1 = (Denotation.InterfaceType) resolve.resolveType("Duck1");
        Denotation.InterfaceType duck2 = (Denotation.InterfaceType) resolve.resolveType("Duck2");
        Assert.assertTrue(duck1.isAssignableFrom(duck2));
        Assert.assertTrue(duck2.isAssignableFrom(duck1));
    }

    @Test
    public void test_subtype() {
        Resolve resolve = new Resolve();
        resolve.define(new DexInterface("" +
                "interface Duck {\n" +
                "  ::Quack(duck: Duck): string\n" +
                "}"));
        resolve.define(new DexInterface("" +
                "interface Donald {\n" +
                "  ::Quack(duck: Donald): string\n" +
                "  ::IAmDonald(duck: Duck): string\n" +
                "}"));
        Denotation.InterfaceType duck = (Denotation.InterfaceType) resolve.resolveType("Duck");
        Denotation.InterfaceType donald = (Denotation.InterfaceType) resolve.resolveType("Donald");
        Assert.assertTrue(duck.isAssignableFrom(donald));
        Assert.assertFalse(donald.isAssignableFrom(duck));
    }
}
