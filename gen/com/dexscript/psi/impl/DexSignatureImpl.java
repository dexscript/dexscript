// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.parser.GoTypes.*;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.dexscript.stubs.GoSignatureStub;

public class DexSignatureImpl extends GoCompositeElementImpl implements DexSignature {

  public DexSignatureImpl(ASTNode node) {
    super(node);
  }

  public DexSignatureImpl(GoSignatureStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitSignature(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public DexParameters getParameters() {
    return notNullChild(PsiTreeUtil.getStubChildOfType(this, DexParameters.class));
  }

  @Override
  @Nullable
  public DexResult getResult() {
    return PsiTreeUtil.getStubChildOfType(this, DexResult.class);
  }

}
