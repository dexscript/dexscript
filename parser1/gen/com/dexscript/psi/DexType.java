// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexTypeStub;

public interface DexType extends DexCompositeElement, StubBasedPsiElement<DexTypeStub> {

  @Nullable
  DexTypeReferenceExpression getTypeReferenceExpression();

  //WARNING: getUnderlyingType(...) is skipped
  //matching getUnderlyingType(DexType, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: shouldGoDeeper(...) is skipped
  //matching shouldGoDeeper(DexType, ...)
  //methods are not found in DexPsiImplUtil

}
