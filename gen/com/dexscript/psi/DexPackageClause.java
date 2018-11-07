// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.GoPackageClauseStub;

public interface DexPackageClause extends GoCompositeElement, StubBasedPsiElement<GoPackageClauseStub> {

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getPackage();

  //WARNING: getName(...) is skipped
  //matching getName(DexPackageClause, ...)
  //methods are not found in null

}
