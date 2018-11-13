package com.dexscript.transpiler;

import com.dexscript.psi.DexCallExpr;
import com.dexscript.psi.DexType;
import com.intellij.openapi.editor.impl.LineSet;
import com.intellij.psi.PsiElement;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.*;

class TranspiledClass extends TranspiledCode {

    private final List<DexCallExpr> refs = new ArrayList<>();
    private final String filename;
    private final LineSet lineSet;
    private final String source;
    private final String shimClassName;
    private final String packageName;
    private final String className;
    private final Map<String, Integer> resultFieldNames = new HashMap<>();

    TranspiledClass(String filename, String source, String packageName, String className) {
        this.filename = filename;
        shimClassName = filename.replace(".", "__");
        lineSet = LineSet.createLineSet(source);
        this.source = source;
        this.packageName = packageName;
        this.className = className;
        append("package ");
        append(packageName);
        append(";\n\n");
    }

    public String qualifiedClassName() {
        return packageName + "." + className;
    }

    public void referenced(DexCallExpr ref) {
        refs.add(ref);
    }

    public void appendSourceLine(PsiElement elem) {
        append("// ");
        append(filename);
        append(':');
        int lineNumber = lineSet.findLineIndex(elem.getNode().getStartOffset());
        append(lineNumber);
        appendNewLine();
        append("// ");
        append(source, lineSet.getLineStart(lineNumber), lineSet.getLineEnd(lineNumber));
        append(prefix);
    }

    public void append(DexType type) {
        append(TransType.translateType(type).className);
    }

    public String className() {
        return className;
    }

    public String shimClassName() {
        return shimClassName;
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

    public String assignResultField(String suggestedFieldName) {
        if (!resultFieldNames.containsKey(suggestedFieldName)) {
            resultFieldNames.put(suggestedFieldName, 1);
            return suggestedFieldName;
        }
        int index = resultFieldNames.get(suggestedFieldName) + 1;
        resultFieldNames.put(suggestedFieldName, index);
        return suggestedFieldName + index;
    }

    public Map<String, Integer> resultFieldNames() {
        return Collections.unmodifiableMap(resultFieldNames);
    }
}
