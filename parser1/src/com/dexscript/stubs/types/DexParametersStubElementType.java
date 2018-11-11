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

import com.dexscript.psi.DexParameters;
import com.dexscript.psi.impl.DexParametersImpl;
import com.dexscript.stubs.DexParametersStub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DexParametersStubElementType extends DexStubElementType<DexParametersStub, DexParameters> {
  public DexParametersStubElementType(@NotNull String name) {
    super(name);
  }

  @NotNull
  @Override
  public DexParameters createPsi(@NotNull DexParametersStub stub) {
    return new DexParametersImpl(stub, this);
  }

  @NotNull
  @Override
  public DexParametersStub createStub(@NotNull DexParameters psi, StubElement parentStub) {
    return new DexParametersStub(parentStub, this, psi.getText());
  }

  @Override
  public void serialize(@NotNull DexParametersStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getText());
  }

  @NotNull
  @Override
  public DexParametersStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new DexParametersStub(parentStub, this, dataStream.readName());
  }
}
