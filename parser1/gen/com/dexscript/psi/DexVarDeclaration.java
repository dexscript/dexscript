// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexVarDeclaration extends DexCompositeElement {

  @NotNull
  List<DexVarSpec> getVarSpecList();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getVar();

  //WARNING: addSpec(...) is skipped
  //matching addSpec(DexVarDeclaration, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: deleteSpec(...) is skipped
  //matching deleteSpec(DexVarDeclaration, ...)
  //methods are not found in DexPsiImplUtil

}
