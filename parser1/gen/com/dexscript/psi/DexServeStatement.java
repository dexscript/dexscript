// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexServeStatement extends DexStatement {

  @Nullable
  DexSignature getSignature();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  PsiElement getServe();

}
