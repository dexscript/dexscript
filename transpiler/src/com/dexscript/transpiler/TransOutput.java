package com.dexscript.transpiler;

import com.dexscript.psi.DexType;
import com.intellij.openapi.editor.impl.LineSet;
import com.intellij.psi.PsiElement;
import org.jaxen.Function;

class TransOutput {

    private final StringBuilder out = new StringBuilder();
    private final String filename;
    private final LineSet lineSet;
    private final String source;
    private String prefix = "";

    TransOutput(String filename, String source) {
        this.filename = filename;
        lineSet = LineSet.createLineSet(source);
        this.source = source;
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
    }

    @Override
    public String toString() {
        return out.toString();
    }


    public interface Operation {
        void call();
    }
}
