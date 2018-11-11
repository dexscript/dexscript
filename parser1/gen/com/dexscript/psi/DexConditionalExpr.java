// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexConditionalExpr extends DexBinaryExpr {

  @NotNull
  PsiElement getRelOp();

}
