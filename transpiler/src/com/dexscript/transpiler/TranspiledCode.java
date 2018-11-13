package com.dexscript.transpiler;

import com.intellij.psi.PsiElement;

public class TranspiledCode {

    private final StringBuilder out = new StringBuilder();
    protected String prefix = "";

    public void indent(Operation op) {
        String oldPrefix = prefix;
        prefix += "  ";
        appendNewLine();
        op.call();
        prefix = oldPrefix;
        appendNewLine();
    }

    public void appendNewLine() {
        out.append(System.lineSeparator());
        out.append(prefix);
    }

    public void append(String text) {
        out.append(text);
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

    public void append(int val) {
        out.append(val);
    }

    public void append(String str, int begin, int end) {
        out.append(str, begin, end);
    }

    public void appendNewLine(char c) {
        append(c);
        appendNewLine();
    }

    public interface Operation {
        void call();
    }

    @Override
    public String toString() {
        return out.toString();
    }
}
