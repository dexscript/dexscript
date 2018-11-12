// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexCallExpr extends DexExpression {

  @NotNull
  DexArgumentList getArgumentList();

  @NotNull
  DexExpression getExpression();

}
