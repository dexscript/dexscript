package com.dexscript.shim.actor;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.shim.OutShim;
import com.dexscript.type.composite.InterfaceType;
import com.dexscript.type.core.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ActorTypeTest {

    private TypeSystem ts;
    private OutShim oShim;

    @Before
    public void setup() {
        ts = new TypeSystem();
        oShim = new OutShim(ts);
    }

    @Test
    public void can_consume_from_actor() {
        ActorType actor = new ActorType(oShim, DexActor.$("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}"));
        InterfaceType inf = new InterfaceType(ts, DexInterface.$("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, actor));
    }

    @Test
    public void await_consumer() {
        ActorType actor = new ActorType(oShim, DexActor.$("" +
                "function Hello() {\n" +
                "   await {\n" +
                "   case AA(): string{\n" +
                "       return 'hello'\n" +
                "   }}\n" +
                "}"));
        InterfaceType inf = new InterfaceType(ts, DexInterface.$("" +
                "interface HasAA {\n" +
                "   AA(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, actor));
        Dispatched dispatched = ts.dispatch(new Invocation("New__", null, new ArrayList<DType>() {{
            add(new StringLiteralType(ts, "AA"));
            add(actor);
        }}, null, null));
        Assert.assertEquals(1, dispatched.candidates.size());
    }

    @Test
    public void return_void() {
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        new ActorType(oShim, DexActor.$("" +
                "function Hello() {\n" +
                "}"));
        Dispatched dispatched = ts.dispatch(new Invocation("Hello",
                null, new ArrayList<>(), null, null));
        Assert.assertEquals(1, dispatched.candidates.size());
        Assert.assertEquals(ts.VOID, dispatched.candidates.get(0).func().ret());
    }

    @Test
    public void generic_type_substitution() {
        new ActorType(oShim, DexActor.$("" +
                "function Hello(<T>: string, msg: T) {\n" +
                "}"));
        Dispatched dispatched = ts.dispatch(new Invocation("Hello",
                null, Arrays.asList(ts.STRING), null, null));
        Assert.assertEquals(1, dispatched.candidates.size());
        DType type = ResolveType.$(ts, "Hello<string>");
        Assert.assertNotNull(type);
    }

    @Test
    public void generic_function_need_expansion() {
        new ActorType(oShim, DexActor.$("" +
                "function Equals(<T>: interface{}, left: T, right: T): bool {\n" +
                "   return true\n" +
                "}"));
        StringLiteralType a = new StringLiteralType(ts, "a");
        StringLiteralType b = new StringLiteralType(ts, "b");
        Dispatched dispatched = ts.dispatch(new Invocation("Equals", null,
                Arrays.asList(a, b), null, null));
        Assert.assertEquals(0, dispatched.candidates.size());
    }
}
