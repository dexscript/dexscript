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
import com.dexscript.stubs.GoTypeStub;

public class DexFunctionTypeImpl extends DexTypeImpl implements DexFunctionType {

  public DexFunctionTypeImpl(ASTNode node) {
    super(node);
  }

  public DexFunctionTypeImpl(GoTypeStub stub, IStubElementType stubType) {
    super(stub, stubType);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitFunctionType(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DexSignature getSignature() {
    return PsiTreeUtil.getStubChildOfType(this, DexSignature.class);
  }

  @Override
  @NotNull
  public PsiElement getFunc() {
    return notNullChild(findChildByType(FUNC));
  }

}
