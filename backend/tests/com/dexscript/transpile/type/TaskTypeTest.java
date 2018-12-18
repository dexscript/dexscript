package com.dexscript.transpile.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.type.DType;
import com.dexscript.type.InterfaceType;
import com.dexscript.type.ResolveType;
import com.dexscript.type.TypeSystem;
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
        Assert.assertTrue(inf.isAssignableFrom(taskType));
    }

    @Test
    public void resolve_string() {
        TypeSystem ts = new TypeSystem();
        DType taskType = ResolveType.$(ts, "Task<string>");
        InterfaceType resolveString = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Resolve__(value: string)\n" +
                "}"));
        Assert.assertTrue(resolveString.isAssignableFrom(taskType));
        InterfaceType resolveInt64 = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Resolve__(value: int64)\n" +
                "}"));
        Assert.assertFalse(resolveInt64.isAssignableFrom(taskType));
    }

    @Test(expected = DexSyntaxException.class)
    public void resolve_generic_type_with_wrong_arguments_count() {
        TypeSystem ts = new TypeSystem();
        ResolveType.$(ts, "Task<string, string>");
    }

}
