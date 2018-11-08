// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.stubs.DexSignatureStub;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class DexSignatureImpl extends DexStubbedElementImpl<DexSignatureStub> implements DexSignature {

  public DexSignatureImpl(@NotNull DexSignatureStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexSignatureImpl(@NotNull ASTNode node) {
    super(node);
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
