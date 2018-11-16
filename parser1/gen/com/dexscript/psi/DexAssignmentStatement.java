// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexAssignmentStatement extends DexStatement {

  @NotNull
  List<DexExpression> getExpressionList();

  @NotNull
  DexLeftHandExprList getLeftHandExprList();

  @NotNull
  DexAssignOp getAssignOp();

}
