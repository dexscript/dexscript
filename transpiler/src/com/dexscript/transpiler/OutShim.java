package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.dexscript.runtime.DexScriptException;

import java.util.*;

public class OutShim extends OutCode {

    public static final String SHIM_PACKAGE = "com.dexscript.gen";
    public static final String SHIM_CLASS = "Shim__";

    private final Set<Pier> piers = new HashSet<>();
    private final Map<Pier, List<Boat>> boats = new HashMap<>();

    public OutShim(DexFile iFile) {
        super(iFile);
    }

    public String generate() {
        append("package ");
        append(SHIM_PACKAGE);
        appendNewLine(";");
        appendNewLine("import com.dexscript.runtime.*;");
        appendNewLine("import static com.dexscript.runtime.DexScriptException.*;");
        appendNewLine();
        append("public class ");
        append(SHIM_CLASS);
        append(" {");
        indent(() -> {
            for (Pier pier : piers) {
                List<Boat> boats = this.boats.get(pier);
                if (boats == null) {
                    String msg = String.format("no implementation of %s with %d arguments",
                            pier.name, pier.argsCount);
                    throw new DexScriptException(msg);
                }
                generate(pier, boats);
            }
        });
        appendNewLine("}");
        return toString();
    }

    private void generate(Pier pier, List<Boat> boats) {
        append("public static Result ");
        append(pier.name);
        append('(');
        appendParamsDeclaration(pier.argsCount);
        append(") {");
        indent(() -> {
            for (Boat boat : boats) {
                append("if (");
                append(boat.check);
                append('(');
                appendParamsInvocation(pier.argsCount);
                append(")) {");
                indent(() -> {
                    append("return ");
                    append(boat.apply);
                    append('(');
                    appendParamsInvocation(pier.argsCount);
                    append(");");
                });
                appendNewLine("}");
                append("throw reportMissingImplementation(\"");
                append(pier.name);
                append('"');
                if (pier.argsCount > 0) {
                    append(", ");
                }
                appendParamsInvocation(pier.argsCount);
                append(");");
            }
        });
        append("}");
    }

//    public void genNewExpr(DexNewExpr iNewExpr) {
//        int paramsCount = iNewExpr.getNewExprArgs().getExpressionList().size();
//        String name = iNewExpr.getExpression().getNode().getText();
//        genCallExpr(name, paramsCount);
//    }
//
//    public void genCallExpr(DexCallExpr iCallExpr) {
//        int paramsCount = iCallExpr.getCallExprArgs().getExpressionList().size();
//        String name = iCallExpr.getExpression().getNode().getText();
//        genCallExpr(name, paramsCount);
//    }
//
//    public void genCallExpr(String name, int paramsCount) {
//        String[] parts = name.split("\\.");
//        String funcName = parts[parts.length - 1];
//        append("public static Result ");
//        append(funcName);
//        if (parts.length == 1) {
//            append('(');
//            appendParamDef(paramsCount);
//            append(") {");
//            indent(() -> {
//                append("return new ");
//                append(name);
//                append('(');
//                appendParamInvocation(paramsCount);
//                append(");");
//            });
//            appendNewLine("}");
//        } else if (parts.length == 2) {
//            append("(Object obj) {");
//            indent(() -> {
//                append("return ((World)obj).");
//                append(funcName);
//                append("();");
//            });
//            appendNewLine("}");
//        } else {
//            throw new UnsupportedOperationException("not implemented");
//        }
//    }
//

    public void addPier(String name, int argsCount) {
        addPier(new Pier(name, argsCount));
    }

    public void addPier(Pier pier) {
        piers.add(pier);
//        String sig = "Add__(Object left, Object right)";
//        if (generated.contains(sig)) {
//            return;
//        }
//        generated.add(sig);
//        append("public static Result ");
//        append(sig);
//        appendNewLine(" {");
//        appendNewLine("  return com.dexscript.runtime.Math.Add__(left, right);");
//        appendNewLine("}");
    }

    public void addBoat(Boat boat) {
        List<Boat> pierBoats = boats.get(boat.pier);
        if (pierBoats == null) {
            pierBoats = new ArrayList<>();
            boats.put(boat.pier, pierBoats);
        }
        pierBoats.add(boat);
    }

//    public void genCastExpr(DexCastExpr iCastExpr) {
//        String sig = "Result Cast__(Object castFrom, Object castToType)";
//        if (generated.contains(sig)) {
//            return;
//        }
//        generated.add(sig);
//        append("public static ");
//        append(sig);
//        appendNewLine(" {");
//        appendNewLine("  return com.dexscript.runtime.Cast.Cast__(castFrom, castToType);");
//        appendNewLine("}");
//    }


}
