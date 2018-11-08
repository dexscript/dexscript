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
import com.dexscript.stubs.DexFileStub;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DexFile extends PsiFileBase {

  public DexFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, DexLanguage.INSTANCE);
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
}