// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.stubs.DexParametersStub;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class DexParametersImpl extends DexStubbedElementImpl<DexParametersStub> implements DexParameters {

  public DexParametersImpl(@NotNull DexParametersStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexParametersImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitParameters(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DexParameterDeclaration> getParameterDeclarationList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, DexParameterDeclaration.class);
  }

  @Override
  @Nullable
  public DexType getType() {
    return PsiTreeUtil.getStubChildOfType(this, DexType.class);
  }

  @Override
  @NotNull
  public PsiElement getLparen() {
    return notNullChild(findChildByType(LPAREN));
  }

  @Override
  @Nullable
  public PsiElement getRparen() {
    return findChildByType(RPAREN);
  }

}
