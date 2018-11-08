// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexParamDeclarationStub;

public interface DexParameterDeclaration extends DexCompositeElement, StubBasedPsiElement<DexParamDeclarationStub> {

  @NotNull
  List<DexParamDefinition> getParamDefinitionList();

  @NotNull
  DexType getType();

  @Nullable
  PsiElement getTripleDot();

  //WARNING: isVariadic(...) is skipped
  //matching isVariadic(DexParameterDeclaration, ...)
  //methods are not found in DexPsiImplUtil

}
