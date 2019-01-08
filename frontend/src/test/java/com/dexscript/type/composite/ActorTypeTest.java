package com.dexscript.type.composite;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.type.DexType;
import com.dexscript.test.framework.FluentAPI;
import com.dexscript.test.framework.Row;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.InferType;
import com.dexscript.type.core.TestAssignable;
import org.junit.Test;

import static com.dexscript.test.framework.TestFramework.stripQuote;
import static com.dexscript.test.framework.TestFramework.testDataFromMySection;

public class ActorTypeTest {

    @Test
    public void no_type_param() {
        testActorType();
    }

    public void testActorType() {
        FakeActorTypeImpl impl = new FakeActorTypeImpl();
        FluentAPI testData = testDataFromMySection();
        for (String code : testData.codes()) {
            if (code.startsWith("interface")) {
                new InterfaceType(impl.typeSystem(), DexInterface.$(code));
            } else {
                new ActorType(impl, DexActor.$(code));
            }
        }
        for (Row row : testData.table().body) {
            boolean isAssignable = "true".equals(row.get(0));
            DType to = InferType.$(impl.typeSystem(), DexType.$parse(stripQuote(row.get(1))));
            DType from = InferType.$(impl.typeSystem(), DexExpr.$parse(stripQuote(row.get(2))));
            TestAssignable.$(isAssignable, to, from);
        }
    }
}
//
//    @Test
//    public void await_consumer() {
//        ActorType actor = new ActorType(oShim, DexActor.$("" +
//                "function Hello() {\n" +
//                "   await {\n" +
//                "   case AA(): string{\n" +
//                "       return 'hello'\n" +
//                "   }}\n" +
//                "}"));
//        InterfaceType inf = new InterfaceType(ts, DexInterface.$("" +
//                "interface HasAA {\n" +
//                "   AA(): string\n" +
//                "}"));
//        Assert.assertTrue(IsAssignable.$(inf, actor));
//        Dispatched dispatched = ts.dispatch(new Invocation("New__", null, new ArrayList<DType>() {{
//            add(new StringLiteralType(ts, "AA"));
//            add(actor);
//        }}, null, null));
//        Assert.assertEquals(1, dispatched.candidates.size());
//    }
//
//    @Test
//    public void return_void() {
//        TypeSystem ts = new TypeSystem();
//        OutShim oShim = new OutShim(ts);
//        new ActorType(oShim, DexActor.$("" +
//                "function Hello() {\n" +
//                "}"));
//        Dispatched dispatched = ts.dispatch(new Invocation("Hello",
//                null, new ArrayList<>(), null, null));
//        Assert.assertEquals(1, dispatched.candidates.size());
//        Assert.assertEquals(ts.VOID, dispatched.candidates.get(0).func().ret());
//    }
//
//    @Test
//    public void generic_type_substitution() {
//        new ActorType(oShim, DexActor.$("" +
//                "function Hello(<T>: string, msg: T) {\n" +
//                "}"));
//        Dispatched dispatched = ts.dispatch(new Invocation("Hello",
//                null, Arrays.asList(ts.STRING), null, null));
//        Assert.assertEquals(1, dispatched.candidates.size());
//        DType type = InferType.$(ts, null, DexType.$parse("Hello<string>"));
//        Assert.assertNotNull(type);
//    }
//
//    @Test
//    public void generic_function_need_expansion() {
//        new ActorType(oShim, DexActor.$("" +
//                "function Equals(<T>: interface{}, left: T, right: T): bool {\n" +
//                "   return true\n" +
//                "}"));
//        StringLiteralType a = new StringLiteralType(ts, "a");
//        StringLiteralType b = new StringLiteralType(ts, "b");
//        Dispatched dispatched = ts.dispatch(new Invocation("Equals", null,
//                Arrays.asList(a, b), null, null));
//        Assert.assertEquals(0, dispatched.candidates.size());
//    }
//}
