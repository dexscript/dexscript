package com.dexscript.transpile.java;

import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.elem.DexSig;
import com.dexscript.ast.elem.DexTypeParam;
import com.dexscript.ast.type.DexParameterizedType;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.java.JavaTypes;
import com.dexscript.transpile.type.java.TranslateSig;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.List;

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
        Assert.assertEquals(1, sig.params().size());
        Assert.assertEquals(TranslateSig.dTypeNameOf(Class1.class), sig.ret().toString());
    }

    public static class Class2 {
        public Class2(String arg0) {
        }
    }

    @Test
    public void one_param() throws Exception {
        Constructor<Class2> ctor = Class2.class.getConstructor(String.class);
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(2, sig.params().size());
        Assert.assertEquals("string", sig.params().get(1).paramType().toString());
    }

    public static class Class3 {
        public Class3(String arg0, Long arg1) {
        }
    }

    @Test
    public void two_params() throws Exception {
        Constructor<Class3> ctor = Class3.class.getConstructor(String.class, Long.class);
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(3, sig.params().size());
        Assert.assertEquals("string", sig.params().get(1).paramType().toString());
        Assert.assertEquals("int64", sig.params().get(2).paramType().toString());
    }

    public static class Class4<T1> {
    }

    @Test
    public void one_type_param() throws Exception {
        Constructor<Class4> ctor = Class4.class.getConstructor();
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(1, sig.typeParams().size());
        DexTypeParam typeParam0 = sig.typeParams().get(0);
        Assert.assertEquals("T1", typeParam0.paramName().toString());
        Assert.assertEquals("interface{}", typeParam0.paramType().toString().trim());
        Assert.assertEquals("T1", sig.ret().asParameterizedType().typeArgs().get(0).toString());
    }

    public static class Class5<T1 extends Long> {
    }

    @Test
    public void type_param_with_bound() throws Exception {
        Constructor<Class5> ctor = Class5.class.getConstructor();
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(1, sig.typeParams().size());
        DexTypeParam typeParam0 = sig.typeParams().get(0);
        Assert.assertEquals("T1", typeParam0.paramName().toString());
        Assert.assertEquals("int64", typeParam0.paramType().toString().trim());
    }

    public static class Class6<T1, T2> {
    }

    @Test
    public void two_type_params() throws Exception {
        Constructor<Class6> ctor = Class6.class.getConstructor();
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(2, sig.typeParams().size());
        DexTypeParam typeParam0 = sig.typeParams().get(0);
        Assert.assertEquals("T1", typeParam0.paramName().toString());
        Assert.assertEquals("interface{}", typeParam0.paramType().toString().trim());
        DexTypeParam typeParam1 = sig.typeParams().get(1);
        Assert.assertEquals("T2", typeParam1.paramName().toString());
        Assert.assertEquals("interface{}", typeParam1.paramType().toString().trim());
    }

    public static class Class7 {
        public <T1> Class7() {
        }
    }

    @Test
    public void type_param_from_ctor() throws Exception {
        Constructor<Class7> ctor = Class7.class.getConstructor();
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(1, sig.typeParams().size());
        DexTypeParam typeParam0 = sig.typeParams().get(0);
        Assert.assertEquals("T1", typeParam0.paramName().toString());
        Assert.assertEquals("interface{}", typeParam0.paramType().toString().trim());
    }

    public static class Class8<T1> {
        public Class8(T1 arg0) {
        }
    }

    @Test
    public void param_ref_class_type_var() throws Exception {
        Constructor<Class8> ctor = Class8.class.getConstructor(Object.class);
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(1, sig.typeParams().size());
        DexTypeParam typeParam0 = sig.typeParams().get(0);
        Assert.assertEquals("T1", typeParam0.paramName().toString());
        Assert.assertEquals("interface{}", typeParam0.paramType().toString().trim());
        Assert.assertEquals(2, sig.params().size());
        DexParam param1 = sig.params().get(1);
        Assert.assertEquals("T1", param1.paramType().toString());
    }

    public static class Class9<T1> {
        public Class9(List<T1> arg0) {
        }
    }

    @Test
    public void param_ref_type_var_expansion() throws Exception {
        Constructor<Class9> ctor = Class9.class.getConstructor(List.class);
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(1, sig.typeParams().size());
        DexTypeParam typeParam0 = sig.typeParams().get(0);
        Assert.assertEquals("T1", typeParam0.paramName().toString());
        Assert.assertEquals("interface{}", typeParam0.paramType().toString().trim());
        Assert.assertEquals(2, sig.params().size());
        DexParam param1 = sig.params().get(1);
        Assert.assertEquals("T1", param1.paramType().asParameterizedType().typeArgs().get(0).toString());
    }

    public static class Class10 {
        public Class10(List<?> arg0) {
        }
    }

    @Test
    public void param_ref_wildcard() throws Exception {
        Constructor<Class10> ctor = Class10.class.getConstructor(List.class);
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(2, sig.params().size());
        DexParam param1 = sig.params().get(1);
        DexParameterizedType parameterizedType = param1.paramType().asParameterizedType();
        Assert.assertEquals("interface{}", parameterizedType.typeArgs().get(0).toString());
    }

    public static class Class11<E> {
        public Class11(List<? extends E> arg0) {
        }
    }

    @Test
    public void param_ref_wildcard_extends() throws Exception {
        Constructor<Class11> ctor = Class11.class.getConstructor(List.class);
        DexSig sig = TranslateSig.$(javaTypes, ctor);
        Assert.assertEquals(2, sig.params().size());
        DexParam param1 = sig.params().get(1);
        DexParameterizedType parameterizedType = param1.paramType().asParameterizedType();
        Assert.assertEquals("E", parameterizedType.typeArgs().get(0).toString());
    }
}
