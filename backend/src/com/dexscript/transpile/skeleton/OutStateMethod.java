package com.dexscript.transpile.skeleton;

import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Line;

public class OutStateMethod implements OutMethod {

    private final Gen g;
    private final OutClass oClass;

    public OutStateMethod(OutClass oClass, int state) {
        this.oClass = oClass;
        oClass.changeMethod(this);
        g = new Gen(oClass.indention());
        g.__("public void "
        ).__(methodName(state)
        ).__("() {");
        g.indention(oClass.indention() + "  ");
        g.__(new Line());
        g.__("Set__state("
        ).__(state
        ).__(new Line(");"));
    }

    public static String methodName(int state) {
        return "State" + state + "__";
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
