package com.dexscript.transpiler;

import com.dexscript.psi.DexFile;
import com.dexscript.psi.DexFunctionDeclaration;
import com.dexscript.psi.DexVisitor;
import com.dexscript.runtime.DexScriptException;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class OutFile extends DexVisitor {

    private final List<OutClass> oClasses = new ArrayList<>();
    private final OutShim oShim;

    OutFile(DexFile iFile, OutShim oShim) {
        this.oShim = oShim;
        iFile.accept(this);
        if (oClasses.size() == 0) {
            throw new DexScriptException("transpile failed");
        }
    }

    @Override
    public void visitFunctionDeclaration(@NotNull DexFunctionDeclaration iFuncDecl) {
        oClasses.add(new OutRootClass(iFuncDecl, oShim));
    }

    @Override
    public void visitFile(PsiFile file) {
        file.acceptChildren(this);
    }

    public List<OutClass> oClasses() {
        return Collections.unmodifiableList(oClasses);
    }
}
