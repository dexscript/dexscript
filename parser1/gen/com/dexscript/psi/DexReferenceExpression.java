// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexReferenceExpression extends DexExpression {

  @NotNull
  PsiElement getIdentifier();

  //WARNING: getReference(...) is skipped
  //matching getReference(DexReferenceExpression, ...)
  //methods are not found in DexPsiImplUtil

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
