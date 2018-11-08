// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexResultStub;

public interface DexResult extends DexCompositeElement, StubBasedPsiElement<DexResultStub> {

  @Nullable
  DexParameters getParameters();

  @Nullable
  DexType getType();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  //WARNING: isVoid(...) is skipped
  //matching isVoid(DexResult, ...)
  //methods are not found in DexPsiImplUtil

}
