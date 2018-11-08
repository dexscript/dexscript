/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Florin Patan
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

import com.dexscript.psi.DexResult;
import com.dexscript.psi.impl.DexResultImpl;
import com.dexscript.stubs.DexResultStub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DexResultStubElementType extends DexStubElementType<DexResultStub, DexResult> {
  public DexResultStubElementType(@NotNull String name) {
    super(name);
  }

  @NotNull
  @Override
  public DexResult createPsi(@NotNull DexResultStub stub) {
    return new DexResultImpl(stub, this);
  }

  @NotNull
  @Override
  public DexResultStub createStub(@NotNull DexResult psi, StubElement parentStub) {
    return new DexResultStub(parentStub, this, psi.getText());
  }

  @Override
  public void serialize(@NotNull DexResultStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getText());
  }

  @NotNull
  @Override
  public DexResultStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new DexResultStub(parentStub, this, dataStream.readName());
  }
}
