package com.dexscript.transpile.java;

import com.dexscript.ast.elem.DexSig;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.java.JavaTypes;
import com.dexscript.transpile.type.java.TranslateSig;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class TranslateSigTest {

    private JavaTypes javaTypes;

    @Before
    public void setup() {
        javaTypes = new JavaTypes(new OutShim(new TypeSystem()));
    }

    public static class Class1 {
    }

    @Test
    public void no_type_param_no_param() throws Exception {
        Constructor<Class1> ctor = Class1.class.getConstructor();
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(0, sig.typeParams().size());
        Assert.assertEquals(0, sig.params().size());
        Assert.assertEquals(TranslateSig.dtypeNameOf(Class1.class), sig.ret().toString());
    }

    public static class Class2 {
        public Class2(String arg0) {
        }
    }

    @Test
    public void one_param() throws Exception {
        Constructor<Class2> ctor = Class2.class.getConstructor(String.class);
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals("string", sig.params().get(0).paramType().toString());
    }

    public static class Class3 {
        public Class3(String arg0, Long arg1) {
        }
    }

    @Test
    public void two_params() throws Exception {
        Constructor<Class3> ctor = Class3.class.getConstructor(String.class, Long.class);
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(2, sig.params().size());
        Assert.assertEquals("string", sig.params().get(0).paramType().toString());
        Assert.assertEquals("int64", sig.params().get(1).paramType().toString());
    }
}
