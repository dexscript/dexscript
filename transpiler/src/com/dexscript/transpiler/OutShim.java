package com.dexscript.transpiler;

import com.dexscript.psi.DexFile;
import com.dexscript.psi.OutCode;
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
            }
            append("throw reportMissingImplementation(\"");
            append(pier.name);
            append('"');
            if (pier.argsCount > 0) {
                append(", ");
            }
            appendParamsInvocation(pier.argsCount);
            append(");");
        });
        appendNewLine("}");
    }

    public void addPier(String name, int argsCount) {
        addPier(new Pier(name, argsCount));
    }

    public void addPier(Pier pier) {
        piers.add(pier);
    }

    public void addBoat(Boat boat) {
        List<Boat> pierBoats = boats.get(boat.pier);
        if (pierBoats == null) {
            pierBoats = new ArrayList<>();
            boats.put(boat.pier, pierBoats);
        }
        pierBoats.add(boat);
    }
}
