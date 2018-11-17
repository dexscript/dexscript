/*
 * Copyright 2013-2016 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dexscript.psi;

import com.dexscript.parser.DexFileType;
import com.dexscript.parser.DexLanguage;
import com.dexscript.psi.impl.DexPsiImplUtil;
import com.dexscript.stubs.DexFileStub;
import com.dexscript.stubs.types.DexFunctionDeclarationStubElementType;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.editor.impl.LineSet;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.util.ArrayFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DexFile extends PsiFileBase {

  private final LineSet lineSet;

  public DexFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, DexLanguage.INSTANCE);
    lineSet = LineSet.createLineSet(viewProvider.getContents());
  }

  @NotNull
  @Override
  public GlobalSearchScope getResolveScope() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    throw new UnsupportedOperationException();
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return DexFileType.INSTANCE;
  }

  @Nullable
  @Override
  public DexFileStub getStub() {
    //noinspection unchecked
    return (DexFileStub)super.getStub();
  }

  public String getPackageName() {
    throw new UnsupportedOperationException();
  }

  public void appendSourceLine(OutCode out, PsiElement elem) {
    out.append("// ");
    out.append(getVirtualFile().getName());
    out.append(':');
    int lineNumber = lineSet.findLineIndex(elem.getNode().getStartOffset());
    out.append(lineNumber);
    out.appendNewLine();
    out.append("// ");
    CharSequence contents = getViewProvider().getContents();
    CharSequence line = contents.subSequence(lineSet.getLineStart(lineNumber), lineSet.getLineEnd(lineNumber));
    out.append(line.toString().trim());
    out.appendNewLine();
  }

  @NotNull
  public List<DexFunctionDeclaration> getFunctions() {
    return CachedValuesManager.getCachedValue(this, () -> {
      DexFileStub stub = getStub();
      List<DexFunctionDeclaration> functions = stub != null
              ? getChildrenByType(stub, DexTypes.FUNCTION_DECLARATION, DexFunctionDeclarationStubElementType.ARRAY_FACTORY)
              : DexPsiImplUtil.dexTraverser().children(this).filter(DexFunctionDeclaration.class).toList();
      return CachedValueProvider.Result.create(functions, this);
    });
  }


  @NotNull
  private static <E extends PsiElement> List<E> getChildrenByType(@NotNull StubElement<? extends PsiElement> stub,
                                                                  IElementType elementType,
                                                                  ArrayFactory<E> f) {
    return Arrays.asList(stub.getChildrenByType(elementType, f));
  }
}