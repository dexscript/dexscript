package com.dexscript.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FunctionTableTest {

    @Test
    public void match_one() {
        FunctionTable functionTable = new FunctionTable();
        FunctionType stringFunc = new FunctionType("Hello", new ArrayList<Type>() {{
            add(BuiltinTypes.STRING);
        }}, BuiltinTypes.VOID);
        functionTable.define(stringFunc);
        FunctionType int64Func = new FunctionType("Hello", new ArrayList<Type>() {{
            add(BuiltinTypes.INT64);
        }}, BuiltinTypes.VOID);
        functionTable.define(int64Func);
        TypeTable typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
        List<FunctionType.Invoked> invokeds = functionTable.invoke(typeTable, "Hello",
                null, new ArrayList<Type>() {{
                    add(BuiltinTypes.STRING);
                }}, null);
        Assert.assertEquals(1, invokeds.size());
        Assert.assertEquals(stringFunc, invokeds.get(0));
    }

    @Test
    public void match_two() {
        FunctionTable functionTable = new FunctionTable();
        FunctionType stringFunc = new FunctionType("Hello", new ArrayList<Type>() {{
            add(BuiltinTypes.STRING);
        }}, BuiltinTypes.VOID);
        functionTable.define(stringFunc);
        FunctionType aFunc = new FunctionType("Hello", new ArrayList<Type>() {{
            add(new StringLiteralType("a"));
        }}, BuiltinTypes.VOID);
        functionTable.define(aFunc);
        TypeTable typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
        List<FunctionType.Invoked> resolved = functionTable.invoke(typeTable, "Hello",
                null, new ArrayList<Type>() {{
                    add(BuiltinTypes.STRING);
                }}, null);
        Assert.assertEquals(2, resolved.size());
    }
}
