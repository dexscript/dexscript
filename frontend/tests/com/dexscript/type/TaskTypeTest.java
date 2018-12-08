package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Test;

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

}
