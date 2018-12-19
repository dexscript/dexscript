package com.dexscript.ast.type;

import org.junit.Assert;
import org.junit.Test;

public class DexParameterizedTypeTest {

    @Test
    public void one_arg() {
        DexParameterizedType type = (DexParameterizedType) DexType.parse("Array<uint8>");
        Assert.assertEquals("Array<uint8>", type.toString());
        Assert.assertEquals("Array", type.genericType().toString());
        Assert.assertEquals(1, type.typeArgs().size());
        Assert.assertEquals("uint8", type.typeArgs().get(0).toString());
    }

    @Test
    public void two_args() {
        DexParameterizedType type = (DexParameterizedType) DexType.parse("Array<uint8, uint16>");
        Assert.assertEquals("Array<uint8, uint16>", type.toString());
        Assert.assertEquals("Array", type.genericType().toString());
        Assert.assertEquals(2, type.typeArgs().size());
        Assert.assertEquals("uint8", type.typeArgs().get(0).toString());
        Assert.assertEquals("uint16", type.typeArgs().get(1).toString());
    }

    @Test
    public void nested_expansion() {
        DexParameterizedType type = (DexParameterizedType) DexType.parse("Array<Array<uint8>>");
        Assert.assertEquals("Array<Array<uint8>>", type.toString());
        Assert.assertEquals("Array<uint8>", type.typeArgs().get(0).toString());
    }

    @Test
    public void missing_type_arg_recover_by_comma() {
        DexParameterizedType type = (DexParameterizedType) DexType.parse("Array<, uint16>");
        Assert.assertEquals("Array<<error/>, uint16>", type.toString());
    }

    @Test
    public void missing_type_arg_recover_by_right_angle_bracket() {
        DexParameterizedType type = (DexParameterizedType) DexType.parse("Array<>");
        Assert.assertEquals("Array<<error/>>", type.toString());
    }

    @Test
    public void missing_comma_recover_by_blank() {
        DexParameterizedType type = (DexParameterizedType) DexType.parse("Array<uint8 uint8>");
        Assert.assertEquals("Array<uint8 <error/>uint8>", type.toString());
    }
}
