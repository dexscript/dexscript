// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexSignatureStub;

public interface DexSignature extends DexCompositeElement, StubBasedPsiElement<DexSignatureStub> {

  @NotNull
  DexParameters getParameters();

  @Nullable
  DexResult getResult();

}
