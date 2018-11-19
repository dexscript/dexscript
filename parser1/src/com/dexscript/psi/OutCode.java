package com.dexscript.psi;

import com.intellij.psi.PsiElement;

import java.util.List;

public class OutCode extends DexVisitor {

    private final DexFile iFile;
    private final StringBuilder out = new StringBuilder();
    protected String prefix = "";

    public OutCode(DexFile iFile) {
        this.iFile = iFile;
    }

    public OutCode(OutCode base) {
        this.iFile = base.iFile;
        this.prefix = base.prefix;
    }

    public DexFile iFile() {
        return iFile;
    }

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

    public void appendSourceLine(PsiElement elem) {
        iFile.appendSourceLine(this, elem);
    }

    public void append(OutCode oCode) {
        append(oCode.toString());
    }

    public void appendParamsDeclaration(int paramsCount) {
        for (int i = 1; i <= paramsCount; i++) {
            if (i != 1) {
                append(", ");
            }
            append("Object arg");
            append(i);
        }
    }

    public void appendParamsInvocation(int paramsCount) {
        for (int i = 1; i <= paramsCount; i++) {
            if (i != 1) {
                append(", ");
            }
            append("arg");
            append(i);
        }
    }

    public void appendParamsDeclaration(DexSignature iSig) {
        int i = 0;
        List<DexParameterDeclaration> iParams = iSig.getParameters().getParameterDeclarationList();
        for (DexParameterDeclaration iParam : iParams) {
            for (DexParamDefinition iParamDef : iParam.getParamDefinitionList()) {
                i++;
                if (i != 1) {
                    append(", ");
                }
                append("Object ");
                append(iParamDef);
            }
        }
    }

    public interface Operation {
        void call();
    }

    @Override
    public String toString() {
        return out.toString();
    }
}
