package com.dexscript.transpile.shim.impl;

import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.transpile.type.CheckType;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ImplEntry {

    private final FunctionType functionType;
    private final String canF;
    private final String callF;
    private final String newF;

    public ImplEntry(FunctionType functionType, String canF, String callF, String newF) {
        functionType.attach(this);
        this.functionType = functionType;
        this.canF = canF;
        this.callF = callF;
        this.newF = newF;
    }

    public String canF() {
        return canF;
    }

    public String callF() {
        return callF;
    }

    public String newF() {
        return newF;
    }

    public FunctionType functionType() {
        return functionType;
    }

    public final void finish(Gen g, CheckType checkType) {
        genCanF(checkType, g, functionType().params());
        genNewF(g);
        genCallF(g);
    }

    protected void genCallF(Gen g) {

    }

    protected void genNewF(Gen g) {

    }

    protected void genCanF(CheckType checkType, Gen g, @NotNull List<Type> params) {
        String canF = OutShim.stripPrefix(canF());
        List<String> typeChecks = new ArrayList<>();
        for (Type param : params) {
            String typeCheck = checkType.$(g, param);
            typeChecks.add(typeCheck);
        }
        g.__("public static boolean "
        ).__(canF);
        DeclareParams.$(g, params.size(), false);
        g.__(" {");
        g.__(new Indent(() -> {
            for (int i = 0; i < typeChecks.size(); i++) {
                String typeCheck = typeChecks.get(i);
                g.__("if (!"
                ).__(typeCheck
                ).__("(arg"
                ).__(i
                ).__(new Line(")) { return false; }"));
            }
            g.__("return true;");
        }));
        g.__(new Line("}"));
    }
}
