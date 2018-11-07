// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.GoParamDefinitionStub;

public interface DexParamDefinition extends GoCompositeElement, StubBasedPsiElement<GoParamDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  //WARNING: isVariadic(...) is skipped
  //matching isVariadic(DexParamDefinition, ...)
  //methods are not found in null

}
