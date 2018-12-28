package com.dexscript.transpile.skeleton;

import com.dexscript.gen.Gen;

public class OutDiscardMethod implements OutMethod {

    private final Gen discard = new Gen();

    public OutDiscardMethod(OutClass oClass) {
        oClass.changeMethod(this);
    }

    @Override
    public Gen g() {
        return discard;
    }

    @Override
    public String finish() {
        return "";
    }
}
