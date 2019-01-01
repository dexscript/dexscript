package com.dexscript.shim.java;

import com.dexscript.ast.elem.DexSig;
import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.java.DefineJavaClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class TranslateJavaCtorTest {

    @Test
    public void no_type_param_no_param() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void one_param() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void two_params() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void one_type_param() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void type_param_with_bound() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void two_type_params() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void type_param_from_ctor() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void param_ref_class_type_var() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void param_ref_type_var_expansion() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void param_ref_wildcard() throws Exception {
        testTranslateJavaCtor();
    }

    @Test
    public void param_ref_wildcard_extends() throws Exception {
        testTranslateJavaCtor();
    }

    private void testTranslateJavaCtor() throws Exception {
        OutTown oTown = new OutTown();
        Class<?> clazz = DefineJavaClass.$(oTown).get("some.java.pkg.SomeClass");
        Method getCtor = clazz.getMethod("ctor");
        Constructor ctor = (Constructor) getCtor.invoke(null);
        DexSig sig = TranslateJavaCtor.$(oTown.oShim().javaTypes(), ctor);
        testDataFromMySection().assertByList(sig);
    }
}
