// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexAssignOp extends DexCompositeElement {

  @Nullable
  PsiElement getAssign();

  @Nullable
  PsiElement getBitAndAssign();

  @Nullable
  PsiElement getBitClearAssign();

  @Nullable
  PsiElement getBitOrAssign();

  @Nullable
  PsiElement getBitXorAssign();

  @Nullable
  PsiElement getMinusAssign();

  @Nullable
  PsiElement getMulAssign();

  @Nullable
  PsiElement getPlusAssign();

  @Nullable
  PsiElement getQuotientAssign();

  @Nullable
  PsiElement getRemainderAssign();

  @Nullable
  PsiElement getShiftLeftAssign();

  @Nullable
  PsiElement getShiftRightAssign();

}
