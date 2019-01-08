package com.dexscript.infer;
//
//import com.dexscript.ast.DexActor;
//import com.dexscript.ast.expr.DexExpr;
//import com.dexscript.type.core.InferType;
//import com.dexscript.shim.OutShim;
//import com.dexscript.type.composite.ActorType;
//import com.dexscript.type.core.DType;
//import com.dexscript.type.core.TypeSystem;
//import org.junit.Assert;
//import org.junit.Test;
//
//public class InferConsumeTest {
//
//    @Test
//    public void consume_string() {
//        TypeSystem ts = new TypeSystem();
//        OutShim oShim = new OutShim(ts);
//
//        new ActorType(oShim, DexActor.$("" +
//                "function Hello(arg: int64): string {\n" +
//                "   return 'hello'\n" +
//                "}"), null);
//        DType type = InferType.$(ts, DexExpr.$parse("<-Hello{100}"));
//        Assert.assertEquals(ts.STRING, type);
//    }
//}
