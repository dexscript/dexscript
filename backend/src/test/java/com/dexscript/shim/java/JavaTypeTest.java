package com.dexscript.shim.java;

import com.dexscript.ast.DexInterface;
import com.dexscript.shim.OutShim;
import com.dexscript.shim.java.JavaType;
import com.dexscript.transpile.OutTown;
import com.dexscript.transpile.java.DefineJavaClass;
import com.dexscript.type.DType;
import com.dexscript.type.InterfaceType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.BaseStream;

import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class JavaTypeTest {

    @Test
    public void just_object() {
        testJavaTypeAssignable();
    }

    @Test
    public void one_param() {
        testJavaTypeAssignable();
    }

    @Test
    public void type_parameters() {
        JavaType dType = new JavaType(new OutShim(new TypeSystem()), BaseStream.class);
        Assert.assertEquals(2, dType.typeParameters().size());
    }

    private static void testJavaTypeAssignable() {
        OutTown oTown = new OutTown();
        Class<?> jType = DefineJavaClass.$(oTown).get("some.java.pkg.SomeClass");
        DType actualDType = oTown.oShim().javaTypes().resolve(jType);
        String code = testDataFromMySection().codes("dexscript").get(0);
        InterfaceType expactedDType = oTown.oShim().typeSystem().defineInterface(DexInterface.$(code));
        TestAssignable.$(true, expactedDType, actualDType);
    }
}
