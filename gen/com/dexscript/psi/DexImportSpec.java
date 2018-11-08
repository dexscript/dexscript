// copyrightHeader.java
package com.dexscript.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.dexscript.stubs.DexImportSpecStub;

public interface DexImportSpec extends DexNamedElement, StubBasedPsiElement<DexImportSpecStub> {

  @NotNull
  DexImportString getImportString();

  @Nullable
  PsiElement getDot();

  @Nullable
  PsiElement getIdentifier();

  //WARNING: getAlias(...) is skipped
  //matching getAlias(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getLocalPackageName(...) is skipped
  //matching getLocalPackageName(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: shouldGoDeeper(...) is skipped
  //matching shouldGoDeeper(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: isForSideEffects(...) is skipped
  //matching isForSideEffects(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: isDot(...) is skipped
  //matching isDot(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getPath(...) is skipped
  //matching getPath(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: getName(...) is skipped
  //matching getName(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

  //WARNING: isCImport(...) is skipped
  //matching isCImport(DexImportSpec, ...)
  //methods are not found in DexPsiImplUtil

}
