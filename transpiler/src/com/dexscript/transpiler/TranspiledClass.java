package com.dexscript.transpiler;

import com.dexscript.psi.DexExpression;
import com.dexscript.psi.DexType;
import com.intellij.openapi.editor.impl.LineSet;
import com.intellij.psi.PsiElement;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.*;

class TranspiledClass extends TranspiledCode {

    private final List<DexExpression> refs = new ArrayList<>();
    private final String filename;
    private final LineSet lineSet;
    private final String source;
    private final String shimClassName;
    private final String packageName;
    private final String className;
    private final Map<String, Integer> fieldNames = new HashMap<>();
    private final List<TranspiledField> fields = new ArrayList<>();

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

    public void referenced(DexExpression ref) {
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
        append(source.substring(lineSet.getLineStart(lineNumber), lineSet.getLineEnd(lineNumber)).trim());
        appendNewLine();
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

    public List<DexExpression> references() {
        return Collections.unmodifiableList(refs);
    }

    public void addToCompiler(InMemoryJavaCompiler compiler) {
        try {
            compiler.addSource(qualifiedClassName(), toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String addField(String originalName, String fieldType) {
        if (!fieldNames.containsKey(originalName)) {
            fieldNames.put(originalName, 1);
            fields.add(new TranspiledField(originalName, originalName, fieldType));
            return originalName;
        }
        int index = fieldNames.get(originalName) + 1;
        fieldNames.put(originalName, index);
        String transpiledName = originalName + index;
        fields.add(new TranspiledField(originalName, transpiledName, fieldType));
        return transpiledName;
    }

    public List<TranspiledField> fields() {
        return Collections.unmodifiableList(fields);
    }
}
