package com.dexscript.shim.java;

import com.dexscript.ast.elem.DexSig;
import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.java.DefineJavaClass;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class TranslateJavaMethodTest {

    @Test
    public void no_type_param_no_param() throws Exception {
        testTranslateJavaMethod();
    }

    @Test
    public void one_param() throws Exception {
        testTranslateJavaMethod();
    }

    @Test
    public void two_params() throws Exception {
        testTranslateJavaMethod();
    }

    @Test
    public void return_string() throws Exception {
        testTranslateJavaMethod();
    }

    @Test
    public void referenced_class_type_param() throws Exception {
        testTranslateJavaMethod();
    }

    @Test
    public void one_type_param() throws Exception {
        testTranslateJavaMethod();
    }

    @Test
    public void referenced_method_type_param() throws Exception {
        testTranslateJavaMethod();
    }

    @Test
    public void getAnnotation_has_two_T() throws Exception {
        testTranslateJavaMethod();
    }

    private void testTranslateJavaMethod() throws Exception {
        OutTown oTown = new OutTown();
        Class<?> clazz = DefineJavaClass.$(oTown).get("some.java.pkg.SomeClass");
        Method getMethod = clazz.getMethod("method");
        Method method = (Method) getMethod.invoke(null);
        DexSig sig = TranslateJavaMethod.$(oTown.oShim().javaTypes(), clazz, method);
        testDataFromMySection().assertByList(sig);
    }
}
