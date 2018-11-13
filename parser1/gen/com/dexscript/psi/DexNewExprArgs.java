// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexNewExprArgs extends DexCompositeElement {

  @NotNull
  List<DexExpression> getExpressionList();

  @NotNull
  PsiElement getLbrace();

  @Nullable
  PsiElement getRbrace();

  @Nullable
  PsiElement getTripleDot();

}
