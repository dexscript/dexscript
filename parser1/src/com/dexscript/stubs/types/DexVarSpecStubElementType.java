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

import com.dexscript.psi.DexFunctionDeclaration;
import com.dexscript.psi.DexTypes;
import com.dexscript.psi.DexVarSpec;
import com.dexscript.psi.impl.DexVarSpecImpl;
import com.dexscript.stubs.DexVarSpecStub;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DexVarSpecStubElementType extends DexStubElementType<DexVarSpecStub, DexVarSpec> {
    public static final DexVarSpec[] EMPTY_ARRAY = new DexVarSpec[0];

    public static final ArrayFactory<DexVarSpec> ARRAY_FACTORY = count -> count == 0 ? EMPTY_ARRAY : new DexVarSpec[count];

    public DexVarSpecStubElementType(@NotNull String name) {
        super(name);
    }

    @NotNull
    @Override
    public DexVarSpec createPsi(@NotNull DexVarSpecStub stub) {
        return new DexVarSpecImpl(stub, this);
    }

    @NotNull
    @Override
    public DexVarSpecStub createStub(@NotNull DexVarSpec psi, StubElement parentStub) {
        return new DexVarSpecStub(parentStub, this);
    }

    @Override
    public boolean shouldCreateStub(ASTNode node) {
        return super.shouldCreateStub(node) &&
                node.getElementType() == DexTypes.VAR_SPEC &&
                PsiTreeUtil.getParentOfType(node.getPsi(), DexFunctionDeclaration.class) == null;
    }

    @Override
    public void serialize(@NotNull DexVarSpecStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    }

    @NotNull
    @Override
    public DexVarSpecStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new DexVarSpecStub(parentStub, this);
    }
}