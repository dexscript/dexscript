// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.dexscript.stubs.DexTypeStub;

public class DexParTypeImpl extends DexTypeImpl implements DexParType {

  public DexParTypeImpl(@NotNull DexTypeStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexParTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitParType(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public DexType getType() {
    return notNullChild(PsiTreeUtil.getStubChildOfType(this, DexType.class));
  }

  @Override
  @NotNull
  public PsiElement getLparen() {
    return notNullChild(findChildByType(LPAREN));
  }

  @Override
  @NotNull
  public PsiElement getRparen() {
    return notNullChild(findChildByType(RPAREN));
  }

}
