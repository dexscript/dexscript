package com.dexscript.transpile;

import com.dexscript.analyze.CheckSemanticError;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexFunction;
import com.dexscript.ast.DexParam;
import com.dexscript.ast.DexRootDecl;
import com.dexscript.ast.expr.DexCallExpr;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.expr.DexReference;
import com.dexscript.resolve.*;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// bookkeeping of the symbols in the town
public class Town {

    public static final String TOWN_CLASSNAME = "Town__";
    public static final String TOWN_QUALIFIED_CLASSNAME = "com.dexscript.runtime.gen__." + TOWN_CLASSNAME;

    private final Map<String, Boat> boats = new HashMap<>();
    private boolean finished;
    private final Gen g = new Gen();
    private Resolve resolve;

    public Town() {
        resolve = new Resolve();
        g.__("package com.dexscript.runtime.gen__"
        ).__(new Line(";"));
        g.__(new Line("import com.dexscript.runtime.*;"));
        g.__("public final class Town__ {");
        g.indention("  ");
        g.__(new Line());
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

    public void define(DexFile iFile) {
        for (DexRootDecl rootDecl : iFile.rootDecls()) {
            if (rootDecl instanceof DexFunction) {
                define((DexFunction) rootDecl);
            }
        }
    }

    private void define(DexFunction iFunction) {
        resolve.define(iFunction);
        List<DexParam> params = iFunction.sig().params();
        Pier pier = new Pier(iFunction.identifier().toString(), params.size());
        Denotation.Type retType = resolveType(iFunction.sig().ret());
        Boat boat = allocate(pier);
        iFunction.attach(boat);
        g.__("public static Result"
        ).__(' '
        ).__(boat.applyF().replace("Town__.", "")
        ).__(new OutSig(this, iFunction.sig())
        ).__(" {"
        ).__(new Indent(() -> {
            g.__("return new "
            ).__(OutClass.qualifiedClassNameOf(iFunction)
            ).__('(');
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) {
                    g.__(", ");
                }
                DexParam param = params.get(i);
                g.__(param.paramName());
            }
            g.__(new Line(");"));
        })).__(new Line("}"));
    }


    private Boat allocate(Pier pier) {
        int i = 1;
        while(true) {
            String boatName = pier.name() + i;
            Boat boat = tryAllocate(pier, boatName);
            if (boat != null) {
                return boat;
            }
            i += 1;
        }
    }

    private Boat tryAllocate(Pier pier, String boatName) {
        if (boats.containsKey(boatName)) {
            return null;
        }
        return new Boat(pier, TOWN_CLASSNAME, boatName);
    }

    public Denotation.Type resolveFunction(DexCallExpr callExpr) {
        return (Denotation.Type) resolve.resolveFunction(callExpr);
    }

    public Denotation.Type resolveType(DexReference ref) {
        return (Denotation.Type) resolve.resolveType(ref);
    }

    public Denotation.Value resolveValue(DexReference ref) {
        return (Denotation.Value) resolve.resolveValue(ref);
    }

    public Denotation.Type resolveType(DexExpr expr) {
        return (Denotation.Type) resolve.resolveType(expr);
    }

    public void checkSemanticError(DexFile iFile) {
        if (new CheckSemanticError(resolve, iFile).hasError()) {
            throw new DexTranspileException();
        }
    }
}
