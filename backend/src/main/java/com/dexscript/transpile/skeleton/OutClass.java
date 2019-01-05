package com.dexscript.transpile.skeleton;

import com.dexscript.gen.Gen;
import com.dexscript.shim.OutShim;
import com.dexscript.type.core.TypeSystem;

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
