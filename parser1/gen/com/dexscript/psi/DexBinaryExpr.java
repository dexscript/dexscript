// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexBinaryExpr extends DexExpression {

  @NotNull
  List<DexExpression> getExpressionList();

  @NotNull
  DexExpression getLeft();

  @Nullable
  DexExpression getRight();

  //WARNING: getOperator(...) is skipped
  //matching getOperator(DexBinaryExpr, ...)
  //methods are not found in DexPsiImplUtil

}
