package com.dexscript.transpile.skeleton;

import com.dexscript.gen.Gen;
import com.dexscript.gen.Line;

public class OutTransientStateMethod implements OutMethod {

    private final Gen g;
    private final OutClass oClass;

    public OutTransientStateMethod(OutClass oClass, int state) {
        this.oClass = oClass;
        oClass.changeMethod(this);
        g = new Gen(oClass.indention());
        g.__("private final void "
        ).__(OutStateMethod.methodName(state)
        ).__("() {");
        g.indention(oClass.indention() + "  ");
        g.__(new Line());
    }

    @Override
    public Gen g() {
        return g;
    }

    @Override
    public String finish() {
        g.indention(oClass.indention());
        g.__(new Line(""));
        g.__(new Line("}"));
        return g.toString();
    }
}
