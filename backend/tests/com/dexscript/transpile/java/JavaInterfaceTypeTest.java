package com.dexscript.transpile.java;

import com.dexscript.ast.DexInterface;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.java.JavaInterfaceType;
import com.dexscript.type.DType;
import com.dexscript.type.IsAssignable;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JavaInterfaceTypeTest {

    private TypeSystem ts;
    private OutShim oShim;

    @Before
    public void setup() {
        ts = new TypeSystem();
        oShim = new OutShim(ts);
    }

    public interface SampleInterface {
        String hello();
    }

    @Test
    public void translate_java_interface() {
        new JavaInterfaceType(oShim, SampleInterface.class);
        DType jInf = ResolveType.$(ts, "SampleInterface");
        DType dInf = ts.defineInterface(new DexInterface("" +
                "interface DexSampleInterface {\n" +
                "   hello(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(jInf, dInf));
        Assert.assertTrue(IsAssignable.$(dInf, jInf));
    }

    public static class SampleClass {
        public String hello() {
            return "";
        }
    }

    @Test
    public void translate_java_class() {
        new JavaInterfaceType(oShim, SampleClass.class);
        DType jInf = ResolveType.$(ts, "SampleClass");
        DType dInf = ts.defineInterface(new DexInterface("" +
                "interface DexSampleInterface {\n" +
                "   hello(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(dInf, jInf));
    }
}
