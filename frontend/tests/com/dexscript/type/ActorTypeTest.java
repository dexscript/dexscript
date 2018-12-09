package com.dexscript.type;

import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ActorTypeTest {

    private ActorType.ImplProvider implProvider = new ActorType.ImplProvider() {
        @Override
        public Object callFunc(FunctionType functionType, DexFunction func) {
            return null;
        }

        @Override
        public Object newFunc(FunctionType functionType, DexFunction func) {
            return null;
        }

        @Override
        public Object innerCallFunc(FunctionType functionType, DexFunction func, DexAwaitConsumer awaitConsumer) {
            return null;
        }

        @Override
        public Object innerNewFunc(FunctionType functionType, DexFunction func, DexAwaitConsumer awaitConsumer) {
            return null;
        }
    };

    @Test
    public void can_consume_from_actor() {
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        ActorTable actorTable = new ActorTable(typeTable);
        FunctionTable functionTable = new FunctionTable();
        ActorType actor = new ActorType(actorTable, functionTable, new DexFunction("" +
                "function Hello(): string {\n" +
                "   return 'hello'\n" +
                "}"), implProvider);
        InterfaceType inf = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface PromiseString {\n" +
                "   Consume__(): string\n" +
                "}"));
        Assert.assertTrue(inf.isAssignableFrom(actor));
        Assert.assertTrue(actor.isAssignableFrom(inf));
    }

    @Test
    public void await_consumer() {
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        ActorTable actorTable = new ActorTable(typeTable);
        FunctionTable functionTable = new FunctionTable();
        ActorType actor = new ActorType(actorTable, functionTable, new DexFunction("" +
                "function Hello() {\n" +
                "   await {\n" +
                "   case AA(): string{\n" +
                "       return 'hello'\n" +
                "   }}\n" +
                "}"), implProvider);
        InterfaceType inf = new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface HasAA {\n" +
                "   AA(): string\n" +
                "}"));
        Assert.assertTrue(inf.isAssignableFrom(actor));
        List<FunctionType> newNestedActor = functionTable.resolve("New__", new ArrayList<Type>() {{
            add(new StringLiteralType("AA"));
            add(actor);
        }});
        Assert.assertEquals(1, newNestedActor.size());
    }

    @Test
    public void return_void() {
        TopLevelTypeTable typeTable = new TopLevelTypeTable(BuiltinTypes.TYPE_TABLE);
        ActorTable actorTable = new ActorTable(typeTable);
        FunctionTable functionTable = new FunctionTable();
        new ActorType(actorTable, functionTable, new DexFunction("" +
                "function Hello() {\n" +
                "}"), implProvider);
        List<FunctionType> functionTypes = functionTable.resolve("Hello", new ArrayList<>());
        Assert.assertEquals(1, functionTypes.size());
        Assert.assertEquals(BuiltinTypes.VOID, functionTypes.get(0).ret());
    }
}
