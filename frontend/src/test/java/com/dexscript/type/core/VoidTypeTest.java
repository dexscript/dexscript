package com.dexscript.type.core;

import com.dexscript.type.core.IsAssignable;
import com.dexscript.type.core.TypeSystem;
import com.dexscript.type.core.VoidType;
import org.junit.Assert;
import org.junit.Test;

public class VoidTypeTest {

    @Test
    public void assignable_to_void() {
        TypeSystem ts = new TypeSystem();
        Assert.assertTrue(IsAssignable.$(new VoidType(ts), ts.VOID));
    }
}
