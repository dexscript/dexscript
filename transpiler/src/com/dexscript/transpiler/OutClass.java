package com.dexscript.transpiler;

import com.dexscript.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class OutClass extends OutCode {

    private final OutShim oShim;
    private final String packageName;
    private final String className;
    private final Namer oFieldNames = new Namer();
    private final Namer oInnerClassNames = new Namer();
    private final List<OutField> oFields = new ArrayList<>();
    private final List<OutMethod> oMethods = new ArrayList<>();
    private final List<OutClass> oInnerClasses = new ArrayList<>();

    public OutClass(DexFile iFile, String className, OutShim oShim) {
        super(iFile);
        this.oShim = oShim;
        this.packageName = iFile.getPackageName();
        this.className = className;
    }

    public OutClass(OutClass oParent, String suggestedClassName) {
        super(oParent);
        oParent.addInnerClass(this);
        this.oShim = oParent.oShim();
        this.packageName = iFile().getPackageName();
        this.className = oInnerClassNames.giveName(suggestedClassName);
    }

    public String qualifiedClassName() {
        return packageName + "." + className;
    }

    public void append(DexType type) {
        append(TransType.translateType(type).className);
    }

    public String className() {
        return className;
    }

    public String packageName() { return packageName; }

    public String addField(String originalName, String fieldType) {
        String transpiledName = oFieldNames.giveName(originalName);
        oFields.add(new OutField(originalName, transpiledName, fieldType));
        return transpiledName;
    }

    public void addMethod(OutMethod oMethod) {
        oMethods.add(oMethod);
    }

    public void addInnerClass(OutClass oInnerClass) {
        oInnerClasses.add(oInnerClass);
    }

    void genClassBody() {
        for (OutMethod oMethod : oMethods) {
            append(oMethod);
            appendNewLine();
        }
        for (OutField field : oFields) {
            append("private ");
            append(field.type);
            append(' ');
            append(field.outName);
            appendNewLine(';');
        }
        for (OutClass oInnerClass : oInnerClasses) {
            append(oInnerClass);
            appendNewLine();
        }
    }

    public void appendReturnValueFields(@NotNull DexSignature iSig) {
        DexResult result = iSig.getResult();
        if (result == null) {
            return;
        }
        DexType returnType = result.getType();
        append("public ");
        append(returnType);
        append(" result1__;");
        appendNewLine();
        append("public Object result1__() {");
        indent(() -> {
            append("return result1__;");
        });
        append("}");
        appendNewLine();
    }

    protected OutShim oShim() {
        return oShim;
    }

    public void addServeBoat(String prefix, DexServeStatement iServeStmt) {
        throw new UnsupportedOperationException("not implemented");
    }
}
