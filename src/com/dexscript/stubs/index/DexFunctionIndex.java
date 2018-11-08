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

package com.dexscript.stubs.index;

import com.dexscript.psi.DexFileElementType;
import com.dexscript.psi.DexFunctionDeclaration;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.intellij.util.indexing.IdFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class DexFunctionIndex extends StringStubIndexExtension<DexFunctionDeclaration> {
  public static final StubIndexKey<String, DexFunctionDeclaration> KEY = StubIndexKey.createIndexKey("go.function");

  @Override
  public int getVersion() {
    return DexFileElementType.VERSION + 3;
  }

  @NotNull
  @Override
  public StubIndexKey<String, DexFunctionDeclaration> getKey() {
    return KEY;
  }

  @NotNull
  public static Collection<DexFunctionDeclaration> find(@NotNull String name,
                                                       @NotNull Project project,
                                                       @Nullable GlobalSearchScope scope,
                                                       @Nullable IdFilter idFilter) {
    return StubIndex.getElements(KEY, name, project, scope, idFilter, DexFunctionDeclaration.class);
  }

  public static boolean process(@NotNull String name,
                                @NotNull Project project,
                                @Nullable GlobalSearchScope scope,
                                @Nullable IdFilter idFilter,
                                @NotNull Processor<DexFunctionDeclaration> processor) {
    return StubIndex.getInstance().processElements(KEY, name, project, scope, idFilter, DexFunctionDeclaration.class, processor);
  }
}
