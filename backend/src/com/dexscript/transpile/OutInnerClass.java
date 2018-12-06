package com.dexscript.transpile;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.method.OutMethod;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public class OutInnerClass implements OutClass {
    @Override
    public TypeSystem typeSystem() {
        return null;
    }

    @Override
    public void changeMethod(OutMethod oMethod) {

    }

    @Override
    public String indention() {
        return null;
    }

    @Override
    public OutField allocateField(String fieldName, Type fieldType) {
        return null;
    }

    @Override
    public OutShim oShim() {
        return null;
    }

    @Override
    public Gen g() {
        return null;
    }
}
