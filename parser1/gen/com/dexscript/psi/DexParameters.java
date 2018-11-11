// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexParametersStub;

public interface DexParameters extends DexCompositeElement, StubBasedPsiElement<DexParametersStub> {

  @NotNull
  List<DexParameterDeclaration> getParameterDeclarationList();

  @Nullable
  DexType getType();

  @NotNull
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

}
