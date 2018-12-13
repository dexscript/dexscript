package com.dexscript.transpile.type.java;

import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JavaClassType implements NamedType, FunctionsProvider {

    private final OutShim oShim;
    private final Class clazz;
    private List<FunctionType> functions;

    public JavaClassType(TypeSystem ts, OutShim oShim, Class clazz) {
        this.oShim = oShim;
        this.clazz = clazz;
        ts.defineType(this);
        ts.lazyDefineFunctions(this);
    }

    @Override
    public @NotNull String name() {
        return clazz.getCanonicalName();
    }

    @Override
    public String javaClassName() {
        return clazz.getCanonicalName();
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean _isSubType(TypeComparisonContext ctx, Type that) {
        return false;
    }

    @Override
    public List<FunctionType> functions() {
        if (functions != null) {
            return functions;
        }
        functions = new ArrayList<>();
        newFuncs(functions);
        return functions;
    }

    private void newFuncs(List<FunctionType> functions) {
        ArrayList<Type> params = new ArrayList<>();
        String funcName = clazz.getSimpleName();
        params.add(new StringLiteralType(funcName));
        FunctionType function = new FunctionType("New__", params, this);
//        function.attach((FunctionType.LazyAttachment) () -> {
//            String callF = OutShim.CLASSNAME + "." + oShim.allocateShim("call__" + funcName);
//            String canF = OutShim.CLASSNAME + "." + oShim.allocateShim("can__" + funcName);
//            return new Impl(function, canF, callF, null);
//        });
        functions.add(function);
    }
}
