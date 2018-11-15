// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexCastExpr extends DexExpression {

  @Nullable
  DexExpression getExpression();

  @NotNull
  DexType getType();

  @NotNull
  PsiElement getLparen();

  @NotNull
  PsiElement getRparen();

}
