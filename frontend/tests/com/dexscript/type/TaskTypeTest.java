package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.core.DexSyntaxException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TaskTypeTest {

    @Test
    public void resolve_any() {
        TypeSystem ts = new TypeSystem();
        Type taskType = ts.resolveType("Task");
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface TaskString {\n" +
                "   Resolve__(value: string)\n" +
                "}"));
        Assert.assertTrue(inf.isAssignableFrom(taskType));
    }

    @Test
    public void resolve_string() {
        TypeSystem ts = new TypeSystem();
        Type taskType = ts.resolveType("Task", Arrays.asList(BuiltinTypes.STRING));
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
        ts.resolveType("Task", Arrays.asList(BuiltinTypes.STRING, BuiltinTypes.STRING));
    }

}
