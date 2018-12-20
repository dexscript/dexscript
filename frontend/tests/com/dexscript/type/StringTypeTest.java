package com.dexscript.type;

import org.junit.Before;
import org.junit.Test;

public class StringTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void relationship() {
        TestAssignable.$(true, new StringType(ts), ts.STRING);
        TestAssignable.$(true, ts.STRING, new StringType(ts));
        TestAssignable.$(true, ts.STRING, ts.literalOf("hello"));
        TestAssignable.$(false, ts.literalOf("hello"), ts.STRING);
        TestAssignable.$(true, ts.literalOf("hello"), ts.literalOf("hello"));

        TestAssignable.$(true, ts.STRING, ts.constOf("hello"));
        TestAssignable.$(true, ts.literalOf("hello"), ts.constOf("hello"));
        TestAssignable.$(false, ts.literalOf("world"), ts.constOf("hello"));
        TestAssignable.$(false, ts.constOf("world"), ts.literalOf("hello"));
    }
}
