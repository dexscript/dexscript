package com.dexscript.transpile.skeleton;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

public interface OutClass {

    TypeSystem typeSystem();

    void changeMethod(OutMethod oMethod);

    String indention();

    OutField allocateField(String fieldName, Type fieldType);

    OutShim oShim();

    Gen g();

    OutStateMachine oStateMachine();

    String className();
}
