// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexImportList extends DexCompositeElement {

  @NotNull
  List<DexImportDeclaration> getImportDeclarationList();

  //WARNING: addImport(...) is skipped
  //matching addImport(DexImportList, ...)
  //methods are not found in DexPsiImplUtil

}
