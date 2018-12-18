package com.dexscript.transpile.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Test;

public class TaskTypeTest {

    @Test
    public void resolve_any() {
        TypeSystem ts = new TypeSystem();
        DType taskType = ResolveType.$(ts, "Task");
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Resolve__(value: string)\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, taskType));
    }

    @Test
    public void resolve_string() {
        TypeSystem ts = new TypeSystem();
        DType taskType = ResolveType.$(ts, "Task<string>");
        InterfaceType resolveString = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Resolve__(value: string)\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(resolveString, taskType));
        InterfaceType resolveInt64 = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Resolve__(value: int64)\n" +
                "}"));
        Assert.assertFalse(IsAssignable.$(resolveInt64, taskType));
    }

    @Test(expected = DexSyntaxException.class)
    public void resolve_generic_type_with_wrong_arguments_count() {
        TypeSystem ts = new TypeSystem();
        ResolveType.$(ts, "Task<string, string>");
    }

}
