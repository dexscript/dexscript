package com.dexscript.type;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.func.DexAwaitConsumer;
import org.junit.Assert;
import org.junit.Test;

public class InnerActorTypeTest {

    @Test
    public void can_consume_from_actor() {
        DexFunction function = new DexFunction("" +
                "function Hello() {\n" +
                "   await {\n" +
                "   case AA(): string{\n" +
                "       return 'hello'\n" +
                "   }}\n" +
                "}");
        DexAwaitConsumer awaitConsumer = (DexAwaitConsumer) function.stmts().get(0).asAwait().cases().get(0);
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
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
