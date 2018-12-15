package com.dexscript.type;

import com.dexscript.ast.DexActor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionTableTest {

    private TypeTable typeTable;
    private FunctionTable functionTable;

    @Before
    public void setup() {
        functionTable = new FunctionTable();
        typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
    }

    public FunctionType func(String actorSrc) {
        DexActor actor = new DexActor("function " + actorSrc);
        FunctionSig sig = new FunctionSig(typeTable, actor.sig());
        FunctionType funcType = new FunctionType(actor.functionName(), sig.params(), sig.ret());
        functionTable.define(funcType);
        return funcType;
    }

    public List<FunctionType.Invoked> invoke(String funcName, String argsStr) {
        return invoke(funcName, null, argsStr);
    }

    public List<FunctionType.Invoked> invoke(String funcName, String typeArgsStr, String argsStr) {
        List<Type> typeArgs = Collections.emptyList();
        if (typeArgsStr != null) {
            typeArgs = ResolveType.$(typeTable, typeArgsStr.split(","));
        }
        List<Type> args = ResolveType.$(typeTable, argsStr.split(","));
        return functionTable.invoke(typeTable, funcName, typeArgs, args, null);
    }

    @Test
    public void match_one() {
        FunctionType func1 = func("Hello(arg0: string)");
        FunctionType func2 = func("Hello(arg0: int64)");
        List<FunctionType.Invoked> invokeds = invoke("Hello", "string");
        Assert.assertEquals(1, invokeds.size());
        Assert.assertEquals(func1, invokeds.get(0).function());
    }

    @Test
    public void match_two() {
        func("Hello(arg0: string)");
        func("Hello(arg0: 'a')");
        List<FunctionType.Invoked> invokeds = invoke("Hello", "string");
        Assert.assertEquals(2, invokeds.size());
    }
}
