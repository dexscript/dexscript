// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.GoImportSpecStub;

public interface DexImportSpec extends GoCompositeElement, StubBasedPsiElement<GoImportSpecStub> {

  @NotNull
  DexImportString getImportString();

  @Nullable
  PsiElement getDot();

  @Nullable
  PsiElement getIdentifier();

  //WARNING: getAlias(...) is skipped
  //matching getAlias(DexImportSpec, ...)
  //methods are not found in null

  //WARNING: getLocalPackageName(...) is skipped
  //matching getLocalPackageName(DexImportSpec, ...)
  //methods are not found in null

  //WARNING: shouldGoDeeper(...) is skipped
  //matching shouldGoDeeper(DexImportSpec, ...)
  //methods are not found in null

  //WARNING: isForSideEffects(...) is skipped
  //matching isForSideEffects(DexImportSpec, ...)
  //methods are not found in null

  //WARNING: isDot(...) is skipped
  //matching isDot(DexImportSpec, ...)
  //methods are not found in null

  //WARNING: getPath(...) is skipped
  //matching getPath(DexImportSpec, ...)
  //methods are not found in null

  //WARNING: getName(...) is skipped
  //matching getName(DexImportSpec, ...)
  //methods are not found in null

  //WARNING: isCImport(...) is skipped
  //matching isCImport(DexImportSpec, ...)
  //methods are not found in null

}
