package com.dexscript.ast.elem;

import org.junit.Assert;
import org.junit.Test;

public class DexTypeParamTest {

    @Test
    public void matched() {
        DexTypeParam typeParam = new DexTypeParam("<T>: string");
        Assert.assertEquals("<T>: string", typeParam.toString());
        Assert.assertEquals("T", typeParam.paramName().toString());
        Assert.assertEquals("string", typeParam.paramType().toString());
    }

    @Test
    public void missing_identifier() {
        DexTypeParam typeParam = new DexTypeParam("<?>: string");
        Assert.assertEquals("<<error/>?>: string", typeParam.toString());
    }

    @Test
    public void missing_right_angle_bracket() {
        DexTypeParam typeParam = new DexTypeParam("<T: string");
        Assert.assertEquals("<T<error/>: string", typeParam.toString());
    }

    @Test
    public void missing_colon() {
        DexTypeParam typeParam = new DexTypeParam("<T> string");
        Assert.assertEquals("<T> <error/>string", typeParam.toString());
    }

    @Test
    public void missing_type() {
        DexTypeParam typeParam = new DexTypeParam("<T>:??");
        Assert.assertEquals("<T>:<error/>??", typeParam.toString());
    }
}
