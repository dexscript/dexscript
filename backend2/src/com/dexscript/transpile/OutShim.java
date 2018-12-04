package com.dexscript.transpile;

import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.type.FunctionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutShim {

    public static final String CLASSNAME = "Shim__";
    public static final String QUALIFIED_CLASSNAME = "com.dexscript.runtime.gen__." + CLASSNAME;
    private boolean finished;
    private final Gen g = new Gen();
    private final Map<String, Integer> shims = new HashMap<>();

    private static class FunctionShims {

        private final String canF;
        private final String callF;
        private final String newF;

        private FunctionShims(String canF, String callF, String newF) {
            this.canF = canF;
            this.callF = callF;
            this.newF = newF;
        }
    }

    public OutShim() {
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
        String canF = defineCan(function);
        String newF = defineNew(function);
        function.attach(new FunctionShims(canF, null, newF));
    }

    private String defineNew(DexFunction function) {
        String shimName = allocateShim("new__" + function.actorName());
        g.__("public static Result "
        ).__(shimName
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
                g.__(param.paramName());
            }
            g.__(");");
        }));
        g.__(new Line("}"));
        return CLASSNAME + "." + shimName;
    }

    private String defineCan(DexFunction function) {
        String shimName = allocateShim("can__" + function.actorName());
        g.__("public static boolean "
        ).__(shimName
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
        return CLASSNAME + "." + shimName;
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
        FunctionShims shims = funcType.definedBy().attachmentOfType(FunctionShims.class);
        return shims.newF;
    }

    public String finish() {
        if (finished) {
            throw new IllegalStateException();
        }
        finished = true;
        g.indention("");
        g.__(new Line());
        g.__(new Line("}"));
        return g.toString();
    }
}
