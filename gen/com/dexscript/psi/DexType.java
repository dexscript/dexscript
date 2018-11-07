// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.GoTypeStub;

public interface DexType extends GoCompositeElement, StubBasedPsiElement<GoTypeStub> {

  @Nullable
  DexTypeReferenceExpression getTypeReferenceExpression();

  //WARNING: getUnderlyingType(...) is skipped
  //matching getUnderlyingType(DexType, ...)
  //methods are not found in null

  //WARNING: shouldGoDeeper(...) is skipped
  //matching shouldGoDeeper(DexType, ...)
  //methods are not found in null

}
