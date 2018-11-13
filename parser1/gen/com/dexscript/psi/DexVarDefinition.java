// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexVarDefinitionStub;

public interface DexVarDefinition extends DexNamedElement, StubBasedPsiElement<DexVarDefinitionStub> {

  @NotNull
  PsiElement getIdentifier();

  //WARNING: getDexTypeInner(...) is skipped
  //matching getDexTypeInner(DexVarDefinition, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getReference(...) is skipped
  //matching getReference(DexVarDefinition, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getValue(...) is skipped
  //matching getValue(DexVarDefinition, ...)
  //methods are not found in DexPsiImplUtil

}
