package com.dexscript.transpile.shim;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.transpile.gen.*;
import com.dexscript.type.FunctionType;
import com.dexscript.type.TypeSystem;

import java.util.*;

public class OutShim {

    public static final String CLASSNAME = "Shim__";
    public static final String QUALIFIED_CLASSNAME = "com.dexscript.runtime.gen__." + CLASSNAME;
    private boolean finished;
    private final TypeSystem ts;
    private final Gen g = new Gen();
    private final Map<String, Integer> shims = new HashMap<>();
    private final List<ActorEntry> actors = new ArrayList<>();
    private final Map<VirtualEntry, List<ConcreteEntry>> impls = new HashMap<>();

    public OutShim(TypeSystem ts) {
        this.ts = ts;
        g.__("package com.dexscript.runtime.gen__"
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__("public final class "
        ).__(CLASSNAME
        ).__(" {");
        g.indention("  ");
        g.__(new Line());
    }

    public void defineFile(DexFile file) {
        for (DexTopLevelDecl topLevelDecl : file.topLevelDecls()) {
            if (topLevelDecl.function() != null) {
                defineActor(topLevelDecl.function());
            }
        }
    }

    public void defineActor(DexFunction function) {
        String newF = allocateShim("new__" + function.actorName());
        String canF = allocateShim("can__" + function.actorName());
        ActorEntry actorEntry = new ActorEntry(impls, function, canF, newF);
        actors.add(actorEntry);
    }

    public String finish() {
        if (finished) {
            throw new IllegalStateException();
        }
        for (ActorEntry actorEntry : actors) {
            DefineNew.$(g, ts, actorEntry.function(), actorEntry.newF());
            DefineCan.$(g, ts, actorEntry.function(), actorEntry.canF());
        }
        for (Map.Entry<VirtualEntry, List<ConcreteEntry>> entry : impls.entrySet()) {
            DefineEntry.$(g, entry.getKey(), entry.getValue());
        }
        finished = true;
        g.indention("");
        g.__(new Line());
        g.__(new Line("}"));
        return g.toString();
    }

    private String allocateShim(String shimName) {
        int count = shims.computeIfAbsent(shimName, k -> 0);
        count += 1;
        return shimName + count;
    }

    public String combineNewF(String funcName, int paramsCount, List<FunctionType> funcTypes) {
        if (funcTypes.size() == 1) {
            FunctionType funcType = funcTypes.get(0);
            ActorEntry shims = funcType.definedBy().attachmentOfType(ActorEntry.class);
            return CLASSNAME + "." + shims.newF();
        }
        String cNewF = allocateShim("cnew__" + funcName);
        g.__("public static Result "
        ).__(cNewF);
        DeclareParams.$(g, paramsCount);
        g.__(" {");
        g.__(new Indent(() -> {
            for (FunctionType funcType : funcTypes) {
                ConcreteEntry concreteEntry = funcType.definedBy().attachmentOfType(ConcreteEntry.class);
                g.__("if ("
                ).__(concreteEntry.canF());
                InvokeParams.$(g, paramsCount);
                g.__(new Line(") {"));
                g.__(new Indent(() -> {
                    g.__("return "
                    ).__(concreteEntry.newF());
                    InvokeParams.$(g, paramsCount);
                    g.__(new Line(";"));
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new RuntimeException();"));
        }));
        g.__(new Line("}"));
        return CLASSNAME + "." + cNewF;
    }
}
