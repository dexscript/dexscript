package com.dexscript.transpile.java;

import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.java.JavaType;
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
