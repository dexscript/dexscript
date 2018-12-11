package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import org.junit.Assert;
import org.junit.Test;

public class InnerActorTypeTest {

    @Test
    public void can_consume_from_actor() {
        DexActor function = new DexActor("" +
                "function Hello() {\n" +
                "   await {\n" +
                "   case AA(): string{\n" +
                "       return 'hello'\n" +
                "   }}\n" +
                "}");
        DexAwaitConsumer awaitConsumer = (DexAwaitConsumer) function.stmts().get(0).asAwait().cases().get(0);
        TypeTable typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
        FunctionTable functionTable = new FunctionTable();
        InnerActorType innerActorType = new InnerActorType(typeTable, functionTable, awaitConsumer);
        InterfaceType inf = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(inf.isAssignableFrom(innerActorType));
        Assert.assertTrue(innerActorType.isAssignableFrom(inf));
    }
}
