// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexConditionalExpr extends DexBinaryExpr {

  @Nullable
  PsiElement getEq();

  @Nullable
  PsiElement getGreater();

  @Nullable
  PsiElement getGreaterOrEqual();

  @Nullable
  PsiElement getLess();

  @Nullable
  PsiElement getLessOrEqual();

  @Nullable
  PsiElement getNotEq();

}
