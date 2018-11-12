package com.dexscript.transpiler;

import com.dexscript.psi.DexCallExpr;
import com.dexscript.psi.DexType;
import com.intellij.openapi.editor.impl.LineSet;
import com.intellij.psi.PsiElement;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TranspiledClass {

    private final StringBuilder out = new StringBuilder();
    private final List<DexCallExpr> refs = new ArrayList<>();
    private final String filename;
    private final LineSet lineSet;
    private final String source;
    private String prefix = "";
    private final String packageName;
    private final String className;

    TranspiledClass(String filename, String source, String packageName, String className) {
        this.filename = filename;
        lineSet = LineSet.createLineSet(source);
        this.source = source;
        this.packageName = packageName;
        this.className = className;
        out.append("package ");
        out.append(packageName);
        out.append(";\n\n");
    }

    public String qualifiedClassName() {
        return packageName + "." + className;
    }

    public void referenced(DexCallExpr ref) {
        refs.add(ref);
    }

    public void appendNewLine() {
        out.append(System.lineSeparator());
        out.append(prefix);
    }

    public void append(String text) {
        out.append(text);
    }

    public void appendSourceLine(PsiElement elem) {
        out.append("// ");
        out.append(filename);
        out.append(':');
        int lineNumber = lineSet.findLineIndex(elem.getNode().getStartOffset());
        out.append(lineNumber);
        appendNewLine();
        out.append("// ");
        out.append(source, lineSet.getLineStart(lineNumber), lineSet.getLineEnd(lineNumber));
        out.append(prefix);
    }

    public void append(DexType type) {
        out.append(translateType(type));
    }

    public void append(char c) {
        out.append(c);
    }

    public void append(PsiElement elem) {
        out.append(elem.getNode().getText());
    }

    public void appendNewLine(String str) {
        append(str);
        appendNewLine();
    }

    public void endActorClass() {
        indent(() -> {
            for (DexCallExpr ref : refs) {
                appendSourceLine(ref);
                append("public static Ctor0Inf ");
                append(ref.getExpression().getNode().getText());
                append("__ctor;");
                appendNewLine();
            }
            appendNewLine("private static boolean inited__;");
            appendNewLine("private static void init__() {");
            appendNewLine("  if (inited__) {");
            appendNewLine("    return;");
            append("  }");
            indent(() -> {
                append("try {");
                indent(() -> {
                    append("Class.forName(\"");
                    append(packageName);
                    append('.');
                    append(filename.replace(".", "__"));
                    append("\");");
                });
                appendNewLine("  inited__ = true;");
                appendNewLine("} catch (Exception e) {");
                appendNewLine("  throw new RuntimeException(e);");
                append("}");
            });
            append("}");
        });
        append('}');
        appendNewLine();
    }

    private String translateType(DexType type) {
        String typeText = type.getNode().getText();
        switch (typeText) {
            case "string":
                return "String";
            default:
                return typeText;
        }
    }

    public void indent(Operation op) {
        String oldPrefix = prefix;
        prefix += "  ";
        appendNewLine();
        op.call();
        prefix = oldPrefix;
        appendNewLine();
    }

    @Override
    public String toString() {
        return out.toString();
    }

    public String className() {
        return className;
    }

    public List<DexCallExpr> references() {
        return Collections.unmodifiableList(refs);
    }

    public void addToCompiler(InMemoryJavaCompiler compiler) {
        try {
            compiler.addSource(qualifiedClassName(), toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface Operation {
        void call();
    }
}
