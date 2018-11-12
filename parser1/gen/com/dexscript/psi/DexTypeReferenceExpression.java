// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexTypeReferenceExpression extends DexCompositeElement {

  @NotNull
  PsiElement getIdentifier();

  //WARNING: getReference(...) is skipped
  //matching getReference(DexTypeReferenceExpression, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getQualifier(...) is skipped
  //matching getQualifier(DexTypeReferenceExpression, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: resolve(...) is skipped
  //matching resolve(DexTypeReferenceExpression, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: resolveType(...) is skipped
  //matching resolveType(DexTypeReferenceExpression, ...)
  //methods are not found in DexPsiImplUtil

}