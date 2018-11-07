// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.GoParametersStub;

public interface DexParameters extends GoCompositeElement, StubBasedPsiElement<GoParametersStub> {

  @NotNull
  List<DexParameterDeclaration> getParameterDeclarationList();

  @Nullable
  DexType getType();

  @NotNull
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

}
