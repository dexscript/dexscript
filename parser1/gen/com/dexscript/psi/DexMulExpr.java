// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexMulExpr extends DexBinaryExpr {

  @Nullable
  PsiElement getBitAnd();

  @Nullable
  PsiElement getBitClear();

  @Nullable
  PsiElement getMul();

  @Nullable
  PsiElement getQuotient();

  @Nullable
  PsiElement getRemainder();

  @Nullable
  PsiElement getShiftLeft();

  @Nullable
  PsiElement getShiftRight();

}
