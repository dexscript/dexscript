package com.dexscript.transpile;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.infer.InferType;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutShim {

    public static final String CLASSNAME = "Shim__";
    public static final String QUALIFIED_CLASSNAME = "com.dexscript.runtime.gen__." + CLASSNAME;
    private boolean finished;
    private final TypeSystem ts;
    private final Gen g = new Gen();
    private final Map<String, Integer> shims = new HashMap<>();
    private final List<ShimF> shimFs = new ArrayList<>();

    private static class ShimF {

        private final DexFunction function;
        private final String canF;
        private final String callF;
        private final String newF;

        private ShimF(DexFunction function, String canF, String callF, String newF) {
            function.attach(this);
            this.function = function;
            this.canF = canF;
            this.callF = callF;
            this.newF = newF;
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
        ShimF shimF = new ShimF(function, canF, null, newF);
        shimFs.add(shimF);
    }

    public String finish() {
        if (finished) {
            throw new IllegalStateException();
        }
        for (ShimF shimF : shimFs) {
            defineNew(shimF.function, shimF.newF);
            defineCan(shimF.function, shimF.canF);
        }
        finished = true;
        g.indention("");
        g.__(new Line());
        g.__(new Line("}"));
        return g.toString();
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
                Type paramType = ts.resolveType(param.paramType());
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
        ShimF shims = funcType.definedBy().attachmentOfType(ShimF.class);
        return CLASSNAME + "." + shims.newF;
    }
}
