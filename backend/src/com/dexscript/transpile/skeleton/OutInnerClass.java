package com.dexscript.transpile.skeleton;

import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.DType;
import com.dexscript.type.TypeSystem;

public class OutInnerClass implements OutClass {

    private final OutShim oShim;
    private final OutClass oOuterClass;
    private final DexAwaitConsumer iAwaitConsumer;
    private final Gen g;
    private final OutStateMachine oStateMachine = new OutStateMachine();
    private OutMethod oMethod;

    public OutInnerClass(OutClass oOuterClass, DexAwaitConsumer iAwaitConsumer) {
        this.oShim = oOuterClass.oShim();
        this.oOuterClass = oOuterClass;
        this.iAwaitConsumer = iAwaitConsumer;
        int outerNextState = oOuterClass.oStateMachine().nextState();
        g = new Gen(oOuterClass.indention());
        g.__("public class "
        ).__(iAwaitConsumer.identifier().toString()
        ).__(" extends Actor {");
        g.__(new Indent(() -> {
            new OutInitMethod(this, iAwaitConsumer);
            oMethod.g().__(oOuterClass.className()
            ).__(".this."
            ).__(OutStateMethod.methodName(outerNextState)
            ).__("();");
            g.__(oMethod.finish());
            oStateMachine.genResumeMethods(g);
        }));
        g.__(new Line("}"));
    }

    @Override
    public TypeSystem typeSystem() {
        return oOuterClass.typeSystem();
    }

    @Override
    public void changeMethod(OutMethod oMethod) {
        if (this.oMethod != null) {
            g.__(this.oMethod.finish());
        }
        this.oMethod = oMethod;
    }

    @Override
    public String indention() {
        return g.indention();
    }

    @Override
    public OutField allocateField(String fieldName) {
        return oOuterClass.allocateField(fieldName);
    }

    @Override
    public OutShim oShim() {
        return oShim;
    }

    @Override
    public Gen g() {
        return oMethod.g();
    }

    @Override
    public OutStateMachine oStateMachine() {
        return oStateMachine;
    }

    @Override
    public String className() {
        return oOuterClass.className() + "." + iAwaitConsumer.identifier().toString();
    }

    @Override
    public String toString() {
        return g.toString();
    }
}
