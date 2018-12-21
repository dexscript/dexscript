package com.dexscript.transpile.type.java;

import com.dexscript.transpile.gen.DeclareParams;
import com.dexscript.transpile.gen.Gen;
import com.dexscript.transpile.gen.Indent;
import com.dexscript.transpile.gen.Line;
import com.dexscript.transpile.shim.OutShim;
import com.dexscript.type.FunctionParam;
import com.dexscript.type.FunctionType;
import com.dexscript.type.DType;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionImpl {

    protected final OutShim oShim;
    protected final FunctionType functionType;
    private String callF;
    private String canF;

    public FunctionImpl(OutShim oShim, FunctionType functionType) {
        this.oShim = oShim;
        this.functionType = functionType;
        oShim.registerImpl(this);
    }

    public boolean hasAwait() {
        return false;
    }

    public String callF() {
        if (callF == null) {
            callF = OutShim.CLASSNAME + "." + genCallF();
        }
        return callF;
    }

    protected abstract String genCallF();

    public String canF(JavaTypes javaTypes) {
        if (canF == null) {
            canF = OutShim.CLASSNAME + "." + genCanF(javaTypes);
        }
        return canF;
    }

    protected String genCanF(JavaTypes javaTypes) {
        String canF = oShim.allocateShim("can__" + functionType.name());
        Gen g = oShim.g();
        List<FunctionParam> params = functionType.params();
        List<String> typeChecks = new ArrayList<>();
        for (FunctionParam param : params) {
            String typeCheck = javaTypes.genTypeCheck(param.type());
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
        return canF;
    }

    public FunctionType functionType() {
        return functionType;
    }
}
