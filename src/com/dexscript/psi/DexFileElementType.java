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

import com.dexscript.parser.DexLanguage;
import com.dexscript.stubs.DexFileStub;
import com.dexscript.stubs.index.DexPackagesIndex;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.*;
import com.intellij.psi.tree.IStubFileElementType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DexFileElementType extends IStubFileElementType<DexFileStub> {
  public static final IStubFileElementType INSTANCE = new DexFileElementType();
  public static final int VERSION = 21;

  private DexFileElementType() {
    super("GO_FILE", DexLanguage.INSTANCE);
  }

  @Override
  public int getStubVersion() {
    return VERSION;
  }

  @NotNull
  @Override
  public StubBuilder getBuilder() {
    return new DefaultStubBuilder() {
      @NotNull
      @Override
      protected StubElement createStubForFile(@NotNull PsiFile file) {
        if (file instanceof DexFile) {
          return new DexFileStub((DexFile)file);
        }
        return super.createStubForFile(file);
      }
    };
  }

  @Override
  public void indexStub(@NotNull DexFileStub stub, @NotNull IndexSink sink) {
    super.indexStub(stub, sink);
    String packageName = stub.getPackageName();
    if (StringUtil.isNotEmpty(packageName)) {
      sink.occurrence(DexPackagesIndex.KEY, packageName);
    }
  }

  @Override
  public void serialize(@NotNull DexFileStub stub, @NotNull StubOutputStream dataStream) throws IOException {
  }

  @NotNull
  @Override
  public DexFileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new DexFileStub(null);
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "go.FILE";
  }
}
