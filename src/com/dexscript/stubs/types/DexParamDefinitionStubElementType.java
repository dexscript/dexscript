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

import com.dexscript.psi.DexParamDefinition;
import com.dexscript.psi.impl.DexParamDefinitionImpl;
import com.dexscript.stubs.DexParamDefinitionStub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DexParamDefinitionStubElementType extends DexNamedStubElementType<DexParamDefinitionStub, DexParamDefinition> {
  public DexParamDefinitionStubElementType(@NotNull String name) {
    super(name);
  }

  @NotNull
  @Override
  public DexParamDefinition createPsi(@NotNull DexParamDefinitionStub stub) {
    return new DexParamDefinitionImpl(stub, this);
  }

  @NotNull
  @Override
  public DexParamDefinitionStub createStub(@NotNull DexParamDefinition psi, StubElement parentStub) {
    return new DexParamDefinitionStub(parentStub, this, psi.getName(), true);
  }

  @Override
  public void serialize(@NotNull DexParamDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
    dataStream.writeBoolean(stub.isPublic());
  }

  @NotNull
  @Override
  public DexParamDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new DexParamDefinitionStub(parentStub, this, dataStream.readName(), dataStream.readBoolean());
  }

  @Override
  protected boolean shouldIndex() {
    return false;
  }
}
