// copyrightHeader.java
package com.dexscript.psi;

import com.dexscript.stubs.DexSignatureStub;
import com.intellij.psi.StubBasedPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DexSignature extends DexCompositeElement, StubBasedPsiElement<DexSignatureStub> {

  @NotNull
  DexParameters getParameters();

  @Nullable
  DexResult getResult();

}
