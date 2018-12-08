package com.dexscript.transpile.shim;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.ast.stmt.DexAwaitStmt;
import com.dexscript.ast.stmt.DexBlock;
import com.dexscript.dispatch.*;
import com.dexscript.transpile.skeleton.OutTopLevelClass;
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
    private final List<InnerActorEntry> innerActors = new ArrayList<>();
    private final DispatchTable dispatchTable = new DispatchTable();

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

    public String finish() {
        if (finished) {
            throw new IllegalStateException();
        }
        for (ActorEntry actorEntry : actors) {
            DefineNew.$(g, ts, actorEntry);
            DefineCan.$(g, ts, actorEntry);
        }
        for (InnerActorEntry innerActor : innerActors) {
            DefineNew.$(g, ts, innerActor);
            DefineCan.$(g, ts, innerActor);
        }
        for (Map.Entry<VirtualEntry, List<ImplEntry>> entry : dispatchTable.entrySet()) {
            DefineEntry.$(g, entry.getKey(), entry.getValue());
        }
        finished = true;
        g.indention("");
        g.__(new Line());
        g.__(new Line("}")); // end of class Shim__
        return g.toString();
    }

    public void defineFile(DexFile file) {
        for (DexTopLevelDecl topLevelDecl : file.topLevelDecls()) {
            if (topLevelDecl.function() != null) {
                defineActor(topLevelDecl.function());
            }
        }
    }

    public void defineActor(DexFunction function) {
        String newF = CLASSNAME + "." + allocateShim("new__" + function.actorName());
        String canF = CLASSNAME + "." + allocateShim("can__" + function.actorName());
        ActorEntry actorEntry = new ActorEntry(dispatchTable, function, canF, newF);
        actors.add(actorEntry);
        new AwaitConsumerCollector(OutTopLevelClass.qualifiedClassNameOf(function)).visit(function.blk());
    }

    private String allocateShim(String shimName) {
        int count = shims.computeIfAbsent(shimName, k -> 0);
        count += 1;
        return shimName + "__" + count;
    }

    public String combineNewF(String funcName, int paramsCount, List<FunctionType> funcTypes) {
        if (funcTypes.size() == 1) {
            FunctionType funcType = funcTypes.get(0);
            return funcType.impl().newF();
        }
        String cNewF = allocateShim("cnew__" + funcName);
        CombineNew.$(g, funcTypes, paramsCount, cNewF);
        return CLASSNAME + "." + cNewF;
    }

    private class AwaitConsumerCollector implements DexElement.Visitor {

        private final String outerClassName;

        public AwaitConsumerCollector(String outerClassName) {
            this.outerClassName = outerClassName;
        }

        @Override
        public void visit(DexElement elem) {
            if (elem instanceof DexBlock || elem instanceof DexAwaitStmt) {
                elem.walkDown(this);
                return;
            }
            if (elem instanceof DexAwaitConsumer) {
                visitAwaitConsumer((DexAwaitConsumer) elem);
            }
        }

        private void visitAwaitConsumer(DexAwaitConsumer awaitConsumer) {
            String funcName = awaitConsumer.identifier().toString();
            String newF = CLASSNAME + "." + allocateShim("new__" + funcName);
            String canF = CLASSNAME + "." + allocateShim("can__" + funcName);
            InnerActorEntry nestedActor = new InnerActorEntry(dispatchTable, outerClassName, awaitConsumer, canF, newF);
            innerActors.add(nestedActor);
        }
    }

    public static String stripPrefix(String f) {
        if (!f.startsWith(OutShim.CLASSNAME)) {
            throw new IllegalArgumentException();
        }
        return f.substring(OutShim.CLASSNAME.length() + 1);
    }
}
