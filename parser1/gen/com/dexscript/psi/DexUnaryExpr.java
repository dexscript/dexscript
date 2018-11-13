// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexUnaryExpr extends DexExpression {

  @Nullable
  DexExpression getExpression();

  @Nullable
  PsiElement getBitAnd();

  @Nullable
  PsiElement getBitXor();

  @Nullable
  PsiElement getMinus();

  @Nullable
  PsiElement getMul();

  @Nullable
  PsiElement getNot();

  @Nullable
  PsiElement getPlus();

  @Nullable
  PsiElement getSendChannel();

  //WARNING: getOperator(...) is skipped
  //matching getOperator(DexUnaryExpr, ...)
  //methods are not found in DexPsiImplUtil

}
