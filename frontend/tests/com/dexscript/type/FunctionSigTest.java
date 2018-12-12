package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.elem.DexSig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FunctionSigTest {

    private TypeTable typeTable;
    private FunctionTable functionTable;

    @Before
    public void setup() {
        typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
        functionTable = new FunctionTable();
    }

    @Test
    public void without_type_params() {
        FunctionSig sig = sig("(arg0: string): int64");
        Assert.assertEquals(BuiltinTypes.INT64, sig.invoke(
                null, resolve("'abc'"), null));
        Assert.assertEquals(BuiltinTypes.INT64, sig.invoke(
                null, resolve("string"), null));
        Assert.assertEquals(BuiltinTypes.UNDEFINED, sig.invoke(
                null, resolve("int64"), null));
    }

    @Test
    public void infer_type_params() {
        FunctionSig sig = sig("(<T>: string, arg0: T): T");
        Type ret = sig.invoke(null, resolve("string"), null);
        Assert.assertEquals(BuiltinTypes.STRING, ret);
    }

    @Test
    public void infer_nested_type_params() {
        defineInterface("" +
                "interface SomeInf {" +
                "   <T>: interface{}\n" +
                "   Get__(arg: T)\n" +
                "}");
        FunctionSig sig = sig("(<T>: string, arg0: SomeInf<T>): SomeInf<T>");
        InterfaceType ret = (InterfaceType) sig.invoke(null, resolve("SomeInf<string>"), null);
        Assert.assertEquals(resolve("string"), ret.typeArgs());
    }

    @Test
    public void infer_deep_nested_type_params() {
        defineInterface("" +
                "interface SomeInf {" +
                "   <T>: interface{}\n" +
                "   Get__(arg: T)\n" +
                "}");
        defineInterface("" +
                "interface AnotherInf {" +
                "   <E1>: interface{};\n" +
                "   <E2>: interface{};\n" +
                "   Get__(index: '0', arg: E1)\n" +
                "   Get__(index: '1', arg: E2)\n" +
                "}");
        FunctionSig sig = sig("(<E1>: string, <E2>: string, arg0: AnotherInf<SomeInf<E1>, SomeInf<E2>>): E2");
        Type ret = sig.invoke(null,
                resolve("AnotherInf<SomeInf<'a'>, SomeInf<'b'>>"), null);
        Assert.assertEquals("'b'", ret.toString());
    }

    @Test
    public void type_parameter_constraint_function() {
        FunctionSig sig = sig("(<T>: interface{}, left: T, right: T): bool");
        Type ret = sig.invoke(null, resolve("string", "int64"), null);
        Assert.assertEquals(BuiltinTypes.UNDEFINED, ret);
    }

    private void defineInterface(String src) {
        new InterfaceType(typeTable, functionTable, new DexInterface(src));
    }

    private FunctionSig sig(String sig) {
        return new FunctionSig(typeTable, new DexSig(sig));
    }

    private List<Type> resolve(String... typeDefs) {
        return ResolveType.$(typeTable, typeDefs);
    }
}
