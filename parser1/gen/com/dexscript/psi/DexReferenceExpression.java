// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.dexscript.psi.impl.DexReference;

public interface DexReferenceExpression extends DexExpression, DexReferenceExpressionBase {

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  DexReference getReference();

  //WARNING: getQualifier(...) is skipped
  //matching getQualifier(DexReferenceExpression, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: resolve(...) is skipped
  //matching resolve(DexReferenceExpression, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getReadWriteAccess(...) is skipped
  //matching getReadWriteAccess(DexReferenceExpression, ...)
  //methods are not found in DexPsiImplUtil

}
