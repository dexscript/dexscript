package com.dexscript.transpiler;

import com.dexscript.psi.DexFunctionDeclaration;
import com.dexscript.psi.DexPackageClause;
import com.dexscript.psi.DexVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

class TranFile extends DexVisitor {

    private final TransOutput out;

    public TranFile(TransOutput out) {
        this.out = out;
    }

    @Override
    public void visitPackageClause(@NotNull DexPackageClause o) {
        out.append(o.getText());
        out.append(";\n\n");
    }

    @Override
    public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration o) {
        out.appendSourceLine(o.getFunction());
        out.append("public class ");
        out.append(o.getIdentifier().getNode().getText());
        out.append(" {\n");
        out.append("}\n");
//        o.acceptChildren(new TransFunc(out));
    }

    @Override
    public void visitFile(PsiFile file) {
        file.acceptChildren(this);
    }
}
