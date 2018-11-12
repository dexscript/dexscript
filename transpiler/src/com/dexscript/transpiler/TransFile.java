package com.dexscript.transpiler;

import com.dexscript.psi.DexFunctionDeclaration;
import com.dexscript.psi.DexPackageClause;
import com.dexscript.psi.DexType;
import com.dexscript.psi.DexVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

class TransFile extends DexVisitor {

    private final TransOutput out;

    public TransFile(TransOutput out) {
        this.out = out;
    }

    @Override
    public void visitPackageClause(@NotNull DexPackageClause o) {
        out.append(o.getText());
        out.append(";\n\n");
        out.append("import com.dexscript.runtime.Actor;\n\n");
    }

    @Override
    public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration o) {
        out.appendSourceLine(o.getFunction());
        out.append("public class ");
        out.append(o.getIdentifier().getNode().getText());
        out.append(" extends Actor {");
        out.indent(() -> {
            out.appendNewLine();
            // fields for return value
            DexType returnType = o.getSignature().getResult().getType();
            out.append("public ");
            out.append(returnType);
            out.append(" result1__;");
            out.appendNewLine();
            out.appendNewLine();
            // constructor
            out.append("public hello() {");
            out.indent(() -> {
                o.getBlock().acceptChildren(new TransFunc(out));
            });
            out.appendNewLine();
            out.append("}");
        });
        out.appendNewLine();
        out.append("}");
        out.appendNewLine();
    }

    @Override
    public void visitFile(PsiFile file) {
        file.acceptChildren(this);
    }
}
