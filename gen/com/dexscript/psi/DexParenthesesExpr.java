// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexParenthesesExpr extends DexExpression {

  @Nullable
  DexExpression getExpression();

  @NotNull
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

}
