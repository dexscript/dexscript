// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexImportDeclaration extends GoCompositeElement {

  @NotNull
  List<DexImportSpec> getImportSpecList();

  @Nullable
  PsiElement getLparen();

  @Nullable
  PsiElement getRparen();

  @NotNull
  PsiElement getImport();

  //WARNING: addImportSpec(...) is skipped
  //matching addImportSpec(DexImportDeclaration, ...)
  //methods are not found in null

}
