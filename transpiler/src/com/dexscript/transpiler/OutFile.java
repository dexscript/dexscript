package com.dexscript.transpiler;

import com.dexscript.psi.*;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class OutFile extends DexVisitor {

    private final List<OutClass> oClasses = new ArrayList<>();
    private final DexFile iFile;
    private String packageName;

    OutFile(DexFile iFile) {
        this.iFile = iFile;
    }

    @Override
    public void visitPackageClause(@NotNull DexPackageClause o) {
        packageName = o.getIdentifier().getNode().getText();
    }

    @Override
    public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration iFuncDecl) {
        oClasses.add(new OutRootClass(iFile, packageName, iFuncDecl));
    }

    @Override
    public void visitFile(PsiFile file) {
        file.acceptChildren(this);
    }

    public List<OutClass> oClasses() {
        return Collections.unmodifiableList(oClasses);
    }

    public void genShim(InMemoryJavaCompiler compiler) {
        new OutShimClass(iFile, packageName, oClasses).addToCompiler(compiler);
    }
}
