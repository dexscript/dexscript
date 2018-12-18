package com.dexscript.transpile.skeleton;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.DType;
import com.dexscript.type.TypeSystem;

public interface OutClass {

    TypeSystem typeSystem();

    void changeMethod(OutMethod oMethod);

    String indention();

    OutField allocateField(String fieldName);

    OutShim oShim();

    Gen g();

    OutStateMachine oStateMachine();

    String className();
}
