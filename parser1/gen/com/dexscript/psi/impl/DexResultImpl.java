// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.stubs.DexResultStub;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class DexResultImpl extends DexStubbedElementImpl<DexResultStub> implements DexResult {

  public DexResultImpl(@NotNull DexResultStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexResultImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitResult(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DexParameters getParameters() {
    return PsiTreeUtil.getStubChildOfType(this, DexParameters.class);
  }

  @Override
  @Nullable
  public DexType getType() {
    return PsiTreeUtil.getStubChildOfType(this, DexType.class);
  }

  @Override
  @Nullable
  public PsiElement getLparen() {
    return findChildByType(LPAREN);
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

}
