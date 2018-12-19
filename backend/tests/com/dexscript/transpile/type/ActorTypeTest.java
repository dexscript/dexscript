package com.dexscript.transpile.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.actor.ActorType;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActorTypeTest {

    @Test
    public void can_consume_from_actor() {
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        ActorType actor = new ActorType(oShim, new DexActor("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}"));
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, actor));
    }

    @Test
    public void await_consumer() {
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        ActorType actor = new ActorType(oShim, new DexActor("" +
                "function Hello() {\n" +
                "   await {\n" +
                "   case AA(): string{\n" +
                "       return 'hello'\n" +
                "   }}\n" +
                "}"));
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface HasAA {\n" +
                "   AA(): string\n" +
                "}"));
        Assert.assertTrue(IsAssignable.$(inf, actor));
        List<FunctionSig.Invoked> newNestedActor = ts.invoke(new Invocation("New__", null, new ArrayList<DType>() {{
            add(new StringLiteralType(ts, "AA"));
            add(actor);
        }}, null));
        Assert.assertEquals(1, newNestedActor.size());
    }

    @Test
    public void return_void() {
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        new ActorType(oShim, new DexActor("" +
                "function Hello() {\n" +
                "}"));
        List<FunctionSig.Invoked> functionTypes = ts.invoke(new Invocation("Hello", null, new ArrayList<>(), null));
        Assert.assertEquals(1, functionTypes.size());
        Assert.assertEquals(ts.VOID, functionTypes.get(0).function().ret());
    }

    @Test
    public void generic_type_substitution() {
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        new ActorType(oShim, new DexActor("" +
                "function Hello(<T>: string, msg: T) {\n" +
                "}"));
        List<FunctionSig.Invoked> functionTypes = ts.invoke(new Invocation("Hello", null, Arrays.asList(ts.STRING), null));
        Assert.assertEquals(1, functionTypes.size());
        DType type = ResolveType.$(ts, "Hello<string>");
        Assert.assertNotNull(type);
    }

    @Test
    public void generic_function_need_expansion() {
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        new ActorType(oShim, new DexActor("" +
                "function Equals(<T>: interface{}, left: T, right: T): bool {\n" +
                "   return true\n" +
                "}"));
        StringLiteralType a = new StringLiteralType(ts, "a");
        StringLiteralType b = new StringLiteralType(ts, "b");
        List<FunctionSig.Invoked> functionTypes = ts.invoke(new Invocation("Equals", null, Arrays.asList(a, b), null));
        Assert.assertEquals(0, functionTypes.size());
    }
}
