package com.dexscript.ast;

import org.junit.Assert;
import org.junit.Test;

public class DexSignatureTest {

    @Test
    public void one_param() {
        DexSignature sig = new DexSignature("(msg:string)");
        Assert.assertEquals("(msg:string)", sig.toString());
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals("msg:string", sig.params().get(0).toString());
    }

    @Test
    public void empty() {
        DexSignature sig = new DexSignature("(){}");
        Assert.assertEquals("()", sig.toString());
        Assert.assertEquals(0, sig.params().size());
    }

    @Test
    public void two_params() {
        DexSignature sig = new DexSignature("(msg1:string, msg2:string)");
        Assert.assertEquals("(msg1:string, msg2:string)", sig.toString());
        Assert.assertEquals(2, sig.params().size());
        Assert.assertEquals("msg1:string", sig.params().get(0).toString());
        Assert.assertEquals("msg2:string", sig.params().get(1).toString());
    }

    @Test
    public void return_value() {
        DexSignature sig = new DexSignature("() : string");
        Assert.assertEquals("() : string", sig.toString());
        Assert.assertEquals(0, sig.params().size());
        Assert.assertEquals("string", sig.ret().toString());
    }

    @Test
    public void invalid_param_name_recover_by_comma() {
        DexSignature sig = new DexSignature("(msg?:string, msg2:string)");
        Assert.assertEquals("(<error/>msg?:string, msg2:string)", sig.toString());
        Assert.assertEquals(2, sig.params().size());
        Assert.assertEquals("<unmatched>msg?:string, msg2:string)</unmatched>", sig.params().get(0).toString());
        Assert.assertEquals("msg2:string", sig.params().get(1).toString());
    }

    @Test
    public void invalid_param_name_recover_by_right_paren() {
        DexSignature sig = new DexSignature("(msg?:string)a");
        Assert.assertEquals("(<error/>msg?:string)", sig.toString());
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals("<unmatched>msg?:string)a</unmatched>", sig.params().get(0).toString());
    }

    @Test
    public void invalid_param_name_recover_by_line_end() {
        DexSignature sig = new DexSignature("(msg?:string\na");
        Assert.assertEquals("(<error/>msg?:string", sig.toString());
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals("<unmatched>msg?:string\na</unmatched>", sig.params().get(0).toString());
    }

    @Test
    public void invalid_param_name_recover_by_file_end() {
        DexSignature sig = new DexSignature("(msg?:string");
        Assert.assertEquals("(<error/>msg?:string", sig.toString());
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals("<unmatched>msg?:string</unmatched>", sig.params().get(0).toString());
    }

    @Test
    public void param_name_missing_colon() {
        DexSignature sig = new DexSignature("(msg string, msg2:string)");
        Assert.assertEquals("(<error/>msg string, msg2:string)", sig.toString());
        Assert.assertEquals(2, sig.params().size());
        Assert.assertEquals("<unmatched>msg string, msg2:string)</unmatched>", sig.params().get(0).toString());
        Assert.assertEquals("msg2:string", sig.params().get(1).toString());
    }

    @Test
    public void param_name_missing_type() {
        DexSignature sig = new DexSignature("(msg:, msg2:string)");
        Assert.assertEquals("(<error/>msg:, msg2:string)", sig.toString());
        Assert.assertEquals(2, sig.params().size());
        Assert.assertEquals("<unmatched>msg:, msg2:string)</unmatched>", sig.params().get(0).toString());
        Assert.assertEquals("msg2:string", sig.params().get(1).toString());
    }

    @Test
    public void missing_right_paren_recover_by_file_end() {
        DexSignature sig = new DexSignature("(msg:string");
        Assert.assertEquals("(msg:string<error/>", sig.toString());
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals("msg:string", sig.params().get(0).toString());
    }

    @Test
    public void missing_right_paren_recover_by_line_end() {
        DexSignature sig = new DexSignature("(msg:string\na");
        Assert.assertEquals("(msg:string<error/>", sig.toString());
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals("msg:string", sig.params().get(0).toString());
    }

    @Test
    public void missing_return_type() {
        DexSignature sig = new DexSignature("():?");
        Assert.assertEquals("():<error/>", sig.toString());
        Assert.assertEquals(0, sig.params().size());
        Assert.assertEquals("<unmatched>?</unmatched>", sig.ret().toString());
    }
}
