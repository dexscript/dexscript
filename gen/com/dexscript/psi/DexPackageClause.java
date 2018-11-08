// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexPackageClauseStub;

public interface DexPackageClause extends DexCompositeElement, StubBasedPsiElement<DexPackageClauseStub> {

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getPackage();

  @Nullable
  String getName();

}
