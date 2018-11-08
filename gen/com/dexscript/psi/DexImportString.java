// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface DexImportString extends DexCompositeElement {

  @NotNull
  DexStringLiteral getStringLiteral();

  //WARNING: getReferences(...) is skipped
  //matching getReferences(DexImportString, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: resolve(...) is skipped
  //matching resolve(DexImportString, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getPath(...) is skipped
  //matching getPath(DexImportString, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getPathTextRange(...) is skipped
  //matching getPathTextRange(DexImportString, ...)
  //methods are not found in DexPsiImplUtil

}
