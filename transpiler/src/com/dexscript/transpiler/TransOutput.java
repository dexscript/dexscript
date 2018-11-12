package com.dexscript.transpiler;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.impl.LineSet;
import com.intellij.psi.PsiElement;

class TransOutput {

    private final StringBuilder out = new StringBuilder();
    private final String filename;
    private final LineSet lineSet;
    private final String source;

    TransOutput(String filename, String source) {
        this.filename = filename;
        lineSet = LineSet.createLineSet(source);
        this.source = source;
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
        out.append("\n");
        out.append("// ");
        out.append(source, lineSet.getLineStart(lineNumber), lineSet.getLineEnd(lineNumber));
    }

    @Override
    public String toString() {
        return out.toString();
    }
}
