package com.dexscript.transpile.skeleton;

import com.dexscript.ast.DexActor;
import com.dexscript.gen.Gen;
import com.dexscript.gen.Indent;
import com.dexscript.gen.Line;
import com.dexscript.shim.OutShim;
import com.dexscript.type.TypeSystem;

public class OutTopLevelClass implements OutClass {

    private final TypeSystem ts;
    private final DexActor iFunc;
    private final Gen g = new Gen();
    private final OutFields oFields = new OutFields();
    private final OutShim oShim;
    private final OutStateMachine stateMachine = new OutStateMachine();
    private OutMethod oMethod;

    public OutTopLevelClass(TypeSystem ts, OutShim oShim, DexActor iFunc) {
        this.ts = ts;
        this.oShim = oShim;
        this.iFunc = iFunc;
        g.__("package "
        ).__(packageName()
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__(new Line("import com.dexscript.runtime.gen.*;"));
        g.__("public class "
        ).__(className()
        ).__(" extends Actor {"
        ).__(new Indent(() -> {
            new OutInitMethod(this, iFunc);
            g.__(oMethod.finish());
            stateMachine.genResumeMethods(g);
            genFields();
        }));
        g.__(new Line("}"));
    }

    private void genFields() {
        for (OutField oField : oFields) {
            g.__("private Object "
            ).__(oField.value()
            ).__(new Line(";"));
        }
    }

    public String packageName() {
        return iFunc.file().packageClause().identifier().toString();
    }

    public String className() {
        return iFunc.actorName();
    }

    public String qualifiedClassName() {
        return packageName() + "." + className();
    }

    @Override
    public String toString() {
        return g.toString();
    }

    public OutField allocateField(String name) {
        return oFields.allocate(name);
    }

    public String indention() {
        return g.indention();
    }

    public TypeSystem typeSystem() {
        return ts;
    }

    public DexActor iFunc() {
        return iFunc;
    }

    public Gen g() {
        return oMethod.g();
    }

    @Override
    public OutStateMachine oStateMachine() {
        return stateMachine;
    }

    public void changeMethod(OutMethod oMethod) {
        if (this.oMethod != null) {
            g.__(this.oMethod.finish());
        }
        this.oMethod = oMethod;
    }

    public OutShim oShim() { return oShim; }
}
