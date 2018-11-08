// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexUnaryExpr extends DexExpression {

  @Nullable
  DexExpression getExpression();

  @NotNull
  PsiElement getUnaryOp();

  //WARNING: getOperator(...) is skipped
  //matching getOperator(DexUnaryExpr, ...)
  //methods are not found in DexPsiImplUtil

}
