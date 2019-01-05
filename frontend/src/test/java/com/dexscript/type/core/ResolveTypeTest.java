package com.dexscript.type.core;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.type.DexType;
import com.dexscript.type.core.ResolveType;
import com.dexscript.type.core.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class ResolveTypeTest {

    @Test
    public void resolve_type_ref() {
        TypeSystem ts = new TypeSystem();
        ts.typeTable().define(new DexPackage("a"), "SomeType", ts.STRING);
        ts.typeTable().define(new DexPackage("b"), "SomeType", ts.STRING);
        Assert.assertEquals(ts.STRING, ResolveType.$(ts, null, DexType.$parse("a.SomeType")));
        Assert.assertEquals(ts.STRING, ResolveType.$(ts, null, DexType.$parse("b.SomeType")));
        Assert.assertEquals(ts.UNDEFINED, ResolveType.$(ts, null, DexType.$parse("c.SomeType")));
    }
}
