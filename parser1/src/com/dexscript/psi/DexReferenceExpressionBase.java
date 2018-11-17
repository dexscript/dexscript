package com.dexscript.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface DexReferenceExpressionBase extends DexCompositeElement {
    @NotNull
    PsiElement getIdentifier();
}
