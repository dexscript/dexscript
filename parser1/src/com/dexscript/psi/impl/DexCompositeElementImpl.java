package com.dexscript.psi.impl;

import com.dexscript.psi.DexCompositeElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class DexCompositeElementImpl extends ASTWrapperPsiElement implements DexCompositeElement {

    public DexCompositeElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean shouldGoDeeper() {
        return false;
    }
}
