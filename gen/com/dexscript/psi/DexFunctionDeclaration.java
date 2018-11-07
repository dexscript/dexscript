// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.GoFunctionDeclarationStub;

public interface DexFunctionDeclaration extends GoCompositeElement, StubBasedPsiElement<GoFunctionDeclarationStub> {

  @Nullable
  DexBlock getBlock();

  @Nullable
  DexSignature getSignature();

  @NotNull
  PsiElement getFunction();

  @NotNull
  PsiElement getIdentifier();

}
