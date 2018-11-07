// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.GoSignatureStub;

public interface DexSignature extends GoCompositeElement, StubBasedPsiElement<GoSignatureStub> {

  @NotNull
  DexParameters getParameters();

  @Nullable
  DexResult getResult();

}
