// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.stubs.DexTypeStub;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class DexTypeImpl extends DexStubbedElementImpl<DexTypeStub> implements DexType {

  public DexTypeImpl(@NotNull DexTypeStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitType(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DexTypeReferenceExpression getTypeReferenceExpression() {
    return PsiTreeUtil.getChildOfType(this, DexTypeReferenceExpression.class);
  }

}
