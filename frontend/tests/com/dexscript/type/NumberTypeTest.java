package com.dexscript.type;

import org.junit.Before;
import org.junit.Test;

public class NumberTypeTest {

    private TypeSystem ts;

    @Before
    public void setup() {
        ts = new TypeSystem();
    }

    @Test
    public void relationship() {
        TestAssignable.$(true, new Int64Type(ts), ts.INT64);
        TestAssignable.$(true, ts.INT64, new Int64Type(ts));
        TestAssignable.$(true, ts.INT64, ts.literalOf(100));
        TestAssignable.$(true, ts.literalOf(100), ts.literalOf(100));
        TestAssignable.$(false, ts.literalOf(100), ts.INT64);
        TestAssignable.$(true, ts.INT64, ts.constOf(100));
        TestAssignable.$(false, ts.constOf(100), ts.INT64);

        TestAssignable.$(true, new Int32Type(ts), ts.INT32);
        TestAssignable.$(true, ts.INT32, new Int32Type(ts));
        TestAssignable.$(false, ts.INT32, ts.INT64);
        TestAssignable.$(false, ts.INT32, ts.literalOf(100));
        TestAssignable.$(true, ts.INT32, ts.constOf(100));
        TestAssignable.$(false, ts.constOf(100), ts.INT32);

        TestAssignable.$(true, new Float64Type(ts), ts.FLOAT64);
        TestAssignable.$(false, ts.FLOAT64, ts.FLOAT32);
        TestAssignable.$(true, ts.FLOAT64, ts.constOf(100.0));
        TestAssignable.$(false, ts.constOf(100.0), ts.FLOAT64);

        TestAssignable.$(true, new Float32Type(ts), ts.FLOAT32);
        TestAssignable.$(false, ts.FLOAT32, ts.FLOAT64);
        TestAssignable.$(true, ts.FLOAT32, ts.constOf(100.0));
        TestAssignable.$(false, ts.constOf(100.0), ts.FLOAT32);

        TestAssignable.$(true, ts.literalOf(100), ts.constOf(100));
        TestAssignable.$(false, ts.literalOf(101), ts.constOf(100));
    }
}
