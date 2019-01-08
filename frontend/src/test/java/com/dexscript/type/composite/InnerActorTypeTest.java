package com.dexscript.type.composite;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.type.core.IsAssignable;
import com.dexscript.type.core.TypeSystem;
import org.junit.Assert;
import org.junit.Test;

public class InnerActorTypeTest {

    @Test
    public void can_consume_from_actor() {
        DexActor function = DexActor.$("" +
                "function Hello() {\n" +
                "   await {\n" +
                "   case AA(): string{\n" +
                "       return 'hello'\n" +
                "   }}\n" +
                "}");
        DexAwaitConsumer awaitConsumer = (DexAwaitConsumer) function.stmts().get(0).asAwait().cases().get(0);
        TypeSystem ts = new TypeSystem();
        InnerActorType innerActorType = new InnerActorType(ts, awaitConsumer);
        InterfaceType inf = new InterfaceType(ts, DexInterface.$("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, innerActorType));
        Assert.assertTrue(IsAssignable.$(innerActorType, inf));
    }
}
