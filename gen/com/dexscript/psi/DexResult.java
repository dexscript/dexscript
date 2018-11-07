// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.GoResultStub;

public interface DexResult extends GoCompositeElement, StubBasedPsiElement<GoResultStub> {

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
  //methods are not found in null

}
