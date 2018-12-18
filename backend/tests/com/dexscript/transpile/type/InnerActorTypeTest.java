package com.dexscript.transpile.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.type.actor.InnerActorType;
import com.dexscript.type.*;
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
        TypeSystem ts = new TypeSystem();
        InnerActorType innerActorType = new InnerActorType(ts, awaitConsumer);
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, innerActorType));
        Assert.assertTrue(IsAssignable.$(innerActorType, inf));
    }
}
