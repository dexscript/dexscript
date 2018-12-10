package com.dexscript.ast.inf;

import org.junit.Assert;
import org.junit.Test;

public class DexInfTypeParamTest {

    @Test
    public void matched() {
        DexInfTypeParam typeArg = new DexInfTypeParam("<T>: string");
        Assert.assertEquals("<T>: string", typeArg.toString());
    }

    @Test
    public void missing_identifier_recover_by_right_angle_bracket() {
        DexInfTypeParam typeArg = new DexInfTypeParam("<>: string");
        Assert.assertEquals("<<error/>>: string", typeArg.toString());
    }

    @Test
    public void missing_identifier_recover_by_blank() {
        DexInfTypeParam typeArg = new DexInfTypeParam("< >: string");
        Assert.assertEquals("<<error/> >: string", typeArg.toString());
    }

    @Test
    public void missing_identifier_recover_by_line_end() {
        DexInfTypeParam typeArg = new DexInfTypeParam("<;>: string");
        Assert.assertEquals("<<error/>", typeArg.toString());
    }

    @Test
    public void missing_right_angle_bracket_recover_by_colon() {
        DexInfTypeParam typeArg = new DexInfTypeParam("<T: string");
        Assert.assertEquals("<T<error/>: string", typeArg.toString());
    }

    @Test
    public void missing_right_angle_bracket_recover_by_blank() {
        DexInfTypeParam typeArg = new DexInfTypeParam("<T : string");
        Assert.assertEquals("<T <error/>: string", typeArg.toString());
    }

    @Test
    public void missing_colon_recover_by_blank() {
        DexInfTypeParam typeArg = new DexInfTypeParam("<T> string");
        Assert.assertEquals("<T> <error/>string", typeArg.toString());
    }

    @Test
    public void missing_type() {
        DexInfTypeParam typeArg = new DexInfTypeParam("<T>:??;abc");
        Assert.assertEquals("<T>:<error/>??", typeArg.toString());
    }
}
