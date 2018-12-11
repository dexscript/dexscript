package com.dexscript.type;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.stmt.DexAwaitConsumer;
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
        ActorType actor = ts.defineActor(new DexActor("" +
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
        ActorType actor = ts.defineActor(new DexActor("" +
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
        List<FunctionType> newNestedActor = ts.resolveFunctions("New__", new ArrayList<Type>() {{
            add(new StringLiteralType("AA"));
            add(actor);
        }});
        Assert.assertEquals(1, newNestedActor.size());
    }

    @Test
    public void return_void() {
        TypeSystem ts = new TypeSystem();
        ts.defineActor(new DexActor("" +
                "function Hello() {\n" +
                "}"), implProvider);
        List<FunctionType> functionTypes = ts.resolveFunctions("Hello", new ArrayList<>());
        Assert.assertEquals(1, functionTypes.size());
        Assert.assertEquals(BuiltinTypes.VOID, functionTypes.get(0).ret());
    }

    @Test
    public void generic_type_substitution() {
        TypeSystem ts = new TypeSystem();
        ts.defineActor(new DexActor("" +
                "function Hello(<T>: string, msg: T) {\n" +
                "}"), implProvider);
        List<FunctionType> functionTypes = ts.resolveFunctions("Hello", Arrays.asList(BuiltinTypes.STRING));
        Assert.assertEquals(1, functionTypes.size());
        Type type = ts.resolveType("Hello", Arrays.asList(BuiltinTypes.STRING));
        Assert.assertNotNull(type);
    }

    @Test
    public void generic_function_need_expansion() {
        TypeSystem ts = new TypeSystem();
        ts.defineActor(new DexActor("" +
                "function Equals(<T>: interface{}, left: T, right: T): bool {\n" +
                "   return true\n" +
                "}"), implProvider);
        StringLiteralType a = new StringLiteralType("a");
        StringLiteralType b = new StringLiteralType("b");
        List<FunctionType> functionTypes = ts.resolveFunctions("Equals", Arrays.asList(a, b));
        Assert.assertEquals(0, functionTypes.size());
    }
}
