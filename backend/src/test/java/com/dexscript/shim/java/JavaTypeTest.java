package com.dexscript.shim.java;

import com.dexscript.shim.OutShim;
import com.dexscript.shim.java.JavaType;
import com.dexscript.type.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.BaseStream;

public class JavaTypeTest {

    @Test
    public void type_parameters() {
        JavaType dType = new JavaType(new OutShim(new TypeSystem()), BaseStream.class);
        Assert.assertEquals(2, dType.typeParameters().size());
    }
}
