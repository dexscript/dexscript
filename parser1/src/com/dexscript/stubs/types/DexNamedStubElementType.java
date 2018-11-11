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

package com.dexscript.stubs.types;

import com.dexscript.psi.DexNamedElement;
import com.dexscript.stubs.DexFileStub;
import com.dexscript.stubs.DexNamedStub;
import com.dexscript.stubs.index.DexAllPrivateNamesIndex;
import com.dexscript.stubs.index.DexAllPublicNamesIndex;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public abstract class DexNamedStubElementType<S extends DexNamedStub<T>, T extends DexNamedElement> extends DexStubElementType<S, T> {
  public DexNamedStubElementType(@NonNls @NotNull String debugName) {
    super(debugName);
  }

  @Override
  public boolean shouldCreateStub(@NotNull ASTNode node) {
    if (!super.shouldCreateStub(node)) return false;
    PsiElement psi = node.getPsi();
    return psi instanceof DexNamedElement && StringUtil.isNotEmpty(((DexNamedElement)psi).getName());
  }

  @Override
  public void indexStub(@NotNull S stub, @NotNull IndexSink sink) {
    String name = stub.getName();
    if (shouldIndex() && StringUtil.isNotEmpty(name)) {
      String packageName = null;
      StubElement parent = stub.getParentStub();
      while (parent != null) {
        if (parent instanceof DexFileStub) {
          packageName = ((DexFileStub)parent).getPackageName();
          break;
        }
        parent = parent.getParentStub();
      }
      
      String indexingName = StringUtil.isNotEmpty(packageName) ? packageName + "." + name : name;
      if (stub.isPublic()) {
        sink.occurrence(DexAllPublicNamesIndex.ALL_PUBLIC_NAMES, indexingName);
      }
      else {
        sink.occurrence(DexAllPrivateNamesIndex.ALL_PRIVATE_NAMES, indexingName);
      }
      for (StubIndexKey<String, ? extends DexNamedElement> key : getExtraIndexKeys()) {
        sink.occurrence(key, name);
      }
    }
  }

  protected boolean shouldIndex() {
    return true;
  }

  @NotNull
  protected Collection<StubIndexKey<String, ? extends DexNamedElement>> getExtraIndexKeys() {
    return Collections.emptyList();
  }
}
