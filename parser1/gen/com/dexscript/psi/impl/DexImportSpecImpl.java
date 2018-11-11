// copyrightHeader.java
package com.dexscript.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dexscript.psi.DexTypes.*;
import com.dexscript.stubs.DexImportSpecStub;
import com.dexscript.psi.*;
import com.intellij.psi.stubs.IStubElementType;

public class DexImportSpecImpl extends DexNamedElementImpl<DexImportSpecStub> implements DexImportSpec {

  public DexImportSpecImpl(@NotNull DexImportSpecStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public DexImportSpecImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DexVisitor visitor) {
    visitor.visitImportSpec(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DexVisitor) accept((DexVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public DexImportString getImportString() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, DexImportString.class));
  }

  @Override
  @Nullable
  public PsiElement getDot() {
    return findChildByType(DOT);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

  public String getAlias() {
    return DexPsiImplUtil.getAlias(this);
  }

  public boolean isDot() {
    return DexPsiImplUtil.isDot(this);
  }

  @NotNull
  public String getPath() {
    return DexPsiImplUtil.getPath(this);
  }

  public boolean isCImport() {
    return DexPsiImplUtil.isCImport(this);
  }

}
