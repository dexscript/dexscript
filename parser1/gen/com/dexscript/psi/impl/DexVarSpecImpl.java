// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.stubs.DexVarSpecStub;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class DexVarSpecImpl extends DexStubbedElementImpl<DexVarSpecStub> implements DexVarSpec {

  public DexVarSpecImpl(@NotNull DexVarSpecStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexVarSpecImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitVarSpec(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<DexExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DexExpression.class);
  }

  @Override
  @Nullable
  public DexType getType() {
    return PsiTreeUtil.getStubChildOfType(this, DexType.class);
  }

  @Override
  @NotNull
  public List<DexVarDefinition> getVarDefinitionList() {
    return PsiTreeUtil.getStubChildrenOfTypeAsList(this, DexVarDefinition.class);
  }

  @Override
  @Nullable
  public PsiElement getAssign() {
    return findChildByType(ASSIGN);
  }

}
