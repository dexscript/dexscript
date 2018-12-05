package com.dexscript.transpile;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.gen.*;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;
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


    private static class VirtualEntry {

        private final String funcName;
        private final int paramsCount;

        private VirtualEntry(String funcName, int paramsCount) {
            this.funcName = funcName;
            this.paramsCount = paramsCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VirtualEntry that = (VirtualEntry) o;
            return paramsCount == that.paramsCount &&
                    Objects.equals(funcName, that.funcName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(funcName, paramsCount);
        }
    }

    private static class ConcreteEntry {

        private final String canF;
        private final String callF;
        private final String newF;

        private ConcreteEntry(String canF, String callF, String newF) {
            this.canF = canF;
            this.callF = callF;
            this.newF = newF;
        }
    }

    private static class ActorEntry extends ConcreteEntry {

        private final DexFunction function;
        private final String canF;
        private final String newF;

        private ActorEntry(Map<VirtualEntry, List<ConcreteEntry>> impls, DexFunction function, String canF, String newF) {
            super(canF, null, newF);
            function.attach(this);
            this.function = function;
            this.canF = canF;
            this.newF = newF;
            VirtualEntry virtualEntry = virtualEntry();
            List<ConcreteEntry> concreteEntries = impls.computeIfAbsent(virtualEntry, k -> new ArrayList<>());
            concreteEntries.add(this);
        }

        private VirtualEntry virtualEntry() {
            return new VirtualEntry(function.functionName(), function.params().size());
        }
    }

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
            defineNew(actorEntry.function, actorEntry.newF);
            defineCan(actorEntry.function, actorEntry.canF);
        }
        for (Map.Entry<VirtualEntry, List<ConcreteEntry>> entry : impls.entrySet()) {
            defineEntry(entry.getKey(), entry.getValue());
        }
        finished = true;
        g.indention("");
        g.__(new Line());
        g.__(new Line("}"));
        return g.toString();
    }

    private void defineEntry(VirtualEntry virtualEntry, List<ConcreteEntry> concreteEntries) {
        g.__("public static Object "
        ).__(virtualEntry.funcName);
        DeclareParams.$(g, virtualEntry.paramsCount);
        g.__(" {");
        g.__(new Indent(() -> {
            for (ConcreteEntry concreteEntry : concreteEntries) {
                g.__("if ("
                ).__(concreteEntry.canF);
                InvokeParams.$(g, virtualEntry.paramsCount);
                g.__(new Line(") {"));
                g.__(new Indent(() -> {
                    g.__("return "
                    ).__(concreteEntry.newF);
                    InvokeParams.$(g, virtualEntry.paramsCount);
                    g.__(new Line(".value();"));
                }));
                g.__(new Line("}"));
            }
            g.__(new Line("throw new RuntimeException();"));
        }));
        g.__(new Line("}"));
    }

    private void defineNew(DexFunction function, String newF) {
        g.__("public static Result "
        ).__(newF
        ).__('(');
        for (int i = 0; i < function.params().size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            DexParam param = function.params().get(i);
            g.__("Object "
            ).__(param.paramName());
        }
        g.__(") {");
        g.__(new Indent(() -> {
            String className = OutClass.qualifiedClassNameOf(function);
            g.__("return new "
            ).__(className
            ).__('(');
            for (int i = 0; i < function.params().size(); i++) {
                if (i > 0) {
                    g.__(", ");
                }
                DexParam param = function.params().get(i);
                Type paramType = InferType.inferType(ts, param.paramType());
                g.__("(("
                ).__(paramType.javaClassName()
                ).__(')'
                ).__(param.paramName()
                ).__(')');
            }
            g.__(");");
        }));
        g.__(new Line("}"));
    }

    private void defineCan(DexFunction function, String canF) {
        g.__("public static boolean "
        ).__(canF
        ).__('(');
        for (int i = 0; i < function.params().size(); i++) {
            if (i > 0) {
                g.__(", ");
            }
            DexParam param = function.params().get(i);
            g.__("Object "
            ).__(param.paramName());
        }
        g.__(") {");
        g.__(new Indent(() -> {
            g.__("return true;");
        }));
        g.__(new Line("}"));
    }

    private String allocateShim(String shimName) {
        int count = shims.computeIfAbsent(shimName, k -> 0);
        count += 1;
        return shimName + count;
    }

    public String newF(List<FunctionType> funcTypes) {
        if (funcTypes.size() != 1) {
            throw new UnsupportedOperationException("not implemented");
        }
        FunctionType funcType = funcTypes.get(0);
        ActorEntry shims = funcType.definedBy().attachmentOfType(ActorEntry.class);
        return CLASSNAME + "." + shims.newF;
    }
}
