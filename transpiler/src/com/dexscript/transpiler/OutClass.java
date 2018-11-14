package com.dexscript.transpiler;

import com.dexscript.psi.DexExpression;
import com.dexscript.psi.DexFile;
import com.dexscript.psi.DexType;
import com.dexscript.psi.OutCode;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.*;

class OutClass extends OutCode {

    private final List<DexExpression> refs = new ArrayList<>();
    private final String shimClassName;
    private final String packageName;
    private final String className;
    private final Map<String, Integer> oFieldNames = new HashMap<>();
    private final List<OutField> oFields = new ArrayList<>();
    private final List<OutMethod> oMethods = new ArrayList<>();

    public OutClass(DexFile iFile, String packageName, String className) {
        super(iFile);
        shimClassName = iFile.getVirtualFile().getName().replace(".", "__");
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
        if (!oFieldNames.containsKey(originalName)) {
            oFieldNames.put(originalName, 1);
            oFields.add(new OutField(originalName, originalName, fieldType));
            return originalName;
        }
        int index = oFieldNames.get(originalName) + 1;
        oFieldNames.put(originalName, index);
        String transpiledName = originalName + index;
        oFields.add(new OutField(originalName, transpiledName, fieldType));
        return transpiledName;
    }

    public List<OutField> oFields() {
        return Collections.unmodifiableList(oFields);
    }

    public List<OutMethod> oMethods() {
        return Collections.unmodifiableList(oMethods);
    }

    public void addMethod(OutMethod oMethod) {
        oMethods.add(oMethod);
    }
}
