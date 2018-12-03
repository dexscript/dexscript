package com.dexscript.transpile;

import com.dexscript.analyze.CheckSemanticError;
import com.dexscript.ast.*;
import com.dexscript.ast.expr.*;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMember;
import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.denotation.BuiltinTypes;
import com.dexscript.denotation.Type;
import com.dexscript.denotation.Value;
import com.dexscript.resolve.*;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;

import java.util.ArrayList;
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
        ArrayList<Type> params = new ArrayList<>();
        params.add(BuiltinTypes.STRING);
        params.add(BuiltinTypes.STRING);
        Denotation.FunctionType addStringString = new Denotation.FunctionType(
                "Add__", null, params, BuiltinTypes.STRING);
        addStringString.setBoat(new Boat(new Pier("Add__", 2), "Add__", "String_String"));
        resolve.define(addStringString);
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

    public void declare(DexFile iFile) {
        for (DexTopLevelDecl rootDecl : iFile.rootDecls()) {
            if (rootDecl.function() != null) {
                DexFunction iFunction = rootDecl.function();
                resolve.define(iFunction);
                List<DexParam> params = iFunction.sig().params();
                Pier pier = new Pier(iFunction.identifier().toString(), params.size());
                Boat boat = allocate(pier);
                iFunction.attach(boat);
            } else if (rootDecl.inf() != null) {
                DexInterface inf = rootDecl.inf();
                resolve.define(inf);
                for (DexInfMember member : inf.members()) {
                    if (member instanceof DexInfMethod) {
                        declare(inf, (DexInfMethod) member);
                    } else if (member instanceof DexInfFunction) {
                        declare(inf, (DexInfFunction) member);
                    }
                }
            }
        }
    }

    private void declare(DexInterface inf, DexInfMethod infMethod) {

    }

    private void declare(DexInterface inf, DexInfFunction infFunction) {
        List<DexParam> params = infFunction.sig().params();
        Pier pier = new Pier(infFunction.identifier().toString(), params.size());
        Boat boat = allocate(pier);
        infFunction.attach(boat);
    }

    public void define(DexFile iFile) {
        for (DexTopLevelDecl rootDecl : iFile.rootDecls()) {
            if (rootDecl.function() != null) {
                define(rootDecl.function());
            } else if (rootDecl.inf() != null) {
                define(rootDecl.inf());
            }
        }
    }

    private void define(DexInterface inf) {
        for (DexInfMember member : inf.members()) {
            if (member instanceof DexInfMethod) {
                define(inf, (DexInfMethod) member);
            } else if (member instanceof DexInfFunction) {
                define(inf, (DexInfFunction) member);
            }
        }
    }

    private void define(DexInterface inf, DexInfMethod iMethod) {

    }

    private void define(DexInterface inf, DexInfFunction infFunction) {
        Boat boat = infFunction.attachmentOfType(Boat.class);
        List<Denotation.FunctionType> impls = resolve.resolveFunctions(infFunction);
        g.__("public static Result"
        ).__(' '
        ).__(boat.applyF().replace("Town__.", "")
        ).__(new OutSig(this, infFunction.sig(), false)
        ).__(" {"
        ).__(new Indent(() -> {
            for (Denotation.FunctionType impl : impls) {
                Boat implBoat = impl.boat();
                g.__("if ("
                ).__(implBoat.checkF()
                ).__(infFunction.sig()
                ).__(") {"
                ).__(new Indent(() -> {
                    g.__("return "
                    ).__(implBoat.applyF()
                    ).__(infFunction.sig()
                    ).__(new Line(";"));
                })).__(new Line("}"));
            }
            g.__(new Line("throw new RuntimeException();"));
        })).__(new Line("}"));
    }

    public void define(DexFunction iFunction) {
        Boat boat = iFunction.attachmentOfType(Boat.class);
        g.__("public static Result "
        ).__(boat.applyF().replace("Town__.", "")
        ).__(new OutSig(this, iFunction.sig(), false)
        ).__(" {"
        ).__(new Indent(() -> {
            g.__("return new "
            ).__(OutClass.qualifiedClassNameOf(iFunction)
            ).__('(');
            List<DexParam> params = iFunction.sig().params();
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) {
                    g.__(", ");
                }
                DexParam param = params.get(i);
                g.__('('
                ).__(resolveType(param.paramType()).javaClassName()
                ).__(')'
                ).__(param.paramName());
            }
            g.__(new Line(");"));
        })).__(new Line("}"));
        g.__("public static boolean can__"
        ).__(boat.applyF().replace("Town__.", "")
        ).__(new OutSig(this, iFunction.sig(), false)
        ).__(" {"
        ).__(new Indent(() -> {
            g.__("return true;");
        })).__(new Line("}"));
    }


    private Boat allocate(Pier pier) {
        int i = 1;
        while (true) {
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
        Boat boat = new Boat(pier, TOWN_CLASSNAME, boatName);
        boats.put(boatName, boat);
        return boat;
    }

    public Denotation.FunctionType resolveFunction(DexFunctionCallExpr callExpr) {
        return (Denotation.FunctionType) resolve.resolveFunction(callExpr);
    }

    public Type resolveType(DexReference ref) {
        Denotation denotation = resolve.resolveType(ref);
        if (denotation instanceof Type) {
            return (Type) denotation;
        }
        throw new DexTranspileException(denotation.toString());
    }

    public Value resolveValue(DexReference ref) {
        return (Value) resolve.resolveValue(ref);
    }

    public Type resolveType(DexExpr expr) {
        return (Type) resolve.resolveType(expr);
    }

    public void checkSemanticError(DexFile iFile) {
        if (new CheckSemanticError(resolve, iFile).hasError()) {
            throw new DexTranspileException();
        }
    }

    public Denotation.FunctionType resolveFunction(DexMethodCallExpr callExpr) {
        return (Denotation.FunctionType) resolve.resolveFunction(callExpr);
    }

    public Denotation.FunctionType resolveFunction(DexNewExpr newExpr) {
        return (Denotation.FunctionType) resolve.resolveFunction(newExpr);
    }

    public Resolve resolve() {
        return resolve;
    }
}
