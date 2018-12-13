package com.dexscript.transpile.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.type.actor.ActorType;
import com.dexscript.type.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActorTypeTest {

    private ActorType.ImplProvider implProvider = new ActorType.ImplProvider() {
        @Override
        public Object callFunc(FunctionType functionType, DexActor func) {
            return null;
        }

        @Override
        public Object newFunc(FunctionType functionType, DexActor func) {
            return null;
        }

        @Override
        public Object innerCallFunc(FunctionType functionType, DexActor func, DexAwaitConsumer awaitConsumer) {
            return null;
        }

        @Override
        public Object innerNewFunc(FunctionType functionType, DexActor func, DexAwaitConsumer awaitConsumer) {
            return null;
        }
    };

    @Test
    public void can_consume_from_actor() {
        TypeSystem ts = new TypeSystem();
        ActorType actor = new ActorType(ts, new DexActor("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}"), implProvider);
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(inf.isAssignableFrom(actor));
    }

    @Test
    public void await_consumer() {
        TypeSystem ts = new TypeSystem();
        ActorType actor = new ActorType(ts, new DexActor("" +
                "function Hello() {\n" +
                "   await {\n" +
                "   case AA(): string{\n" +
                "       return 'hello'\n" +
                "   }}\n" +
                "}"), implProvider);
        InterfaceType inf = ts.defineInterface(new DexInterface("" +
                "interface HasAA {\n" +
                "   AA(): string\n" +
                "}"));
        Assert.assertTrue(inf.isAssignableFrom(actor));
        List<FunctionType.Invoked> newNestedActor = ts.invoke("New__", null, new ArrayList<Type>() {{
            add(new StringLiteralType("AA"));
            add(actor);
        }}, null);
        Assert.assertEquals(1, newNestedActor.size());
    }

    @Test
    public void return_void() {
        TypeSystem ts = new TypeSystem();
        new ActorType(ts, new DexActor("" +
                "function Hello() {\n" +
                "}"), implProvider);
        List<FunctionType.Invoked> functionTypes = ts.invoke("Hello",
                null, new ArrayList<>(), null);
        Assert.assertEquals(1, functionTypes.size());
        Assert.assertEquals(BuiltinTypes.VOID, functionTypes.get(0).ret());
    }

    @Test
    public void generic_type_substitution() {
        TypeSystem ts = new TypeSystem();
        new ActorType(ts, new DexActor("" +
                "function Hello(<T>: string, msg: T) {\n" +
                "}"), implProvider);
        List<FunctionType.Invoked> functionTypes = ts.invoke("Hello",
                null, Arrays.asList(BuiltinTypes.STRING), null);
        Assert.assertEquals(1, functionTypes.size());
        Type type = ts.resolveType("Hello", Arrays.asList(BuiltinTypes.STRING));
        Assert.assertNotNull(type);
    }

    @Test
    public void generic_function_need_expansion() {
        TypeSystem ts = new TypeSystem();
        new ActorType(ts, new DexActor("" +
                "function Equals(<T>: interface{}, left: T, right: T): bool {\n" +
                "   return true\n" +
                "}"), implProvider);
        StringLiteralType a = new StringLiteralType("a");
        StringLiteralType b = new StringLiteralType("b");
        List<FunctionType.Invoked> functionTypes = ts.invoke("Equals",
                null, Arrays.asList(a, b), null);
        Assert.assertEquals(0, functionTypes.size());
    }
}
