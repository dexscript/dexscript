package com.dexscript.type;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import org.junit.Assert;
import org.junit.Test;

public class ActorTypeTest {

    @Test
    public void can_consume_from_actor() {
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        ActorTable actorTable = new ActorTable(typeTable);
        FunctionTable functionTable = new FunctionTable();
        ActorType actor = new ActorType(actorTable, functionTable, new DexFunction("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}"));
        InterfaceType inf = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(inf.isAssignableFrom(actor));
        Assert.assertTrue(actor.isAssignableFrom(inf));
    }
}
