// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexReplyStatement extends DexStatement {

  @NotNull
  List<DexExpression> getExpressionList();

  @NotNull
  PsiElement getReply();

}
