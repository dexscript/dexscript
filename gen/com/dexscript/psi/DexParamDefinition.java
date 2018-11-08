// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexParamDefinitionStub;

public interface DexParamDefinition extends DexNamedElement, StubBasedPsiElement<DexParamDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  boolean isVariadic();

}
