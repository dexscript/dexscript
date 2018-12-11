package com.dexscript.type;

import com.dexscript.ast.DexInterface;
import com.dexscript.ast.type.DexGenericExpansionType;
import com.dexscript.ast.type.DexType;
import com.dexscript.ast.type.DexTypeRef;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionSigTest {

    @Test
    public void without_type_params() {
        TypeTable typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
        FunctionSig sig = new FunctionSig(
                typeTable, null, Arrays.asList(BuiltinTypes.STRING),
                BuiltinTypes.INT64, DexType.parse("int64"));
        Assert.assertEquals(BuiltinTypes.INT64, sig.invoke(
                Collections.emptyList(), Arrays.asList(new StringLiteralType("abc")), null));
        Assert.assertEquals(BuiltinTypes.INT64, sig.invoke(
                Collections.emptyList(), Arrays.asList(BuiltinTypes.STRING), null));
        Assert.assertEquals(BuiltinTypes.UNDEFINED, sig.invoke(
                Collections.emptyList(), Arrays.asList(BuiltinTypes.INT64), null));
    }

    @Test
    public void infer_type_params() {
        TypeTable typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
        PlaceholderType T = new PlaceholderType("T", BuiltinTypes.STRING);
        List<PlaceholderType> typeParams = Arrays.asList(T);
        List<Type> params = Arrays.asList(T);
        FunctionSig sig = new FunctionSig(typeTable, typeParams, params, T, new DexTypeRef("T"));
        List<Type> args = Arrays.asList(BuiltinTypes.STRING);
        Type ret = sig.invoke(Collections.emptyList(), args, null);
        Assert.assertEquals(BuiltinTypes.STRING, ret);
    }

    @Test
    public void infer_nested_type_params() {
        TypeTable typeTable = new TypeTable(BuiltinTypes.TYPE_TABLE);
        FunctionTable functionTable = new FunctionTable();
        new InterfaceType(typeTable, functionTable, new DexInterface("" +
                "interface SomeInf {" +
                "   <T>: interface{}\n" +
                "   Get__(arg: T)\n" +
                "}"));
        PlaceholderType T = new PlaceholderType("T", BuiltinTypes.STRING);
        List<PlaceholderType> typeParams = Arrays.asList(T);
        List<Type> params = Arrays.asList(typeTable.resolveType("SomeInf", Arrays.asList(T)));
        FunctionSig sig = new FunctionSig(typeTable, typeParams, params, T, DexType.parse("SomeInf<T>"));
        List<Type> args = Arrays.asList(typeTable.resolveType("SomeInf", Arrays.asList(BuiltinTypes.STRING)));
        InterfaceType ret = (InterfaceType) sig.invoke(Collections.emptyList(), args, null);
        Assert.assertEquals(Arrays.asList(BuiltinTypes.STRING), ret.typeArgs());
    }
}
