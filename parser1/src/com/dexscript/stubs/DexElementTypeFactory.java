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

package com.dexscript.stubs;

import com.dexscript.psi.DexType;
import com.dexscript.psi.impl.DexFunctionTypeImpl;
import com.dexscript.psi.impl.DexParTypeImpl;
import com.dexscript.psi.impl.DexTypeImpl;
import com.dexscript.psi.impl.DexTypeListImpl;
import com.dexscript.stubs.types.*;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.containers.HashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DexElementTypeFactory {
  private static final Map<String, Class> TYPES = new HashMap<String, Class>() {
    {
      put("FUNCTION_TYPE", DexFunctionTypeImpl.class);
      put("TYPE", DexTypeImpl.class);
      put("PAR_TYPE", DexParTypeImpl.class);
      put("TYPE_LIST", DexTypeListImpl.class);
    }
  };

  private DexElementTypeFactory() {}

  public static IStubElementType stubFactory(@NotNull String name) {
    if ("FUNCTION_DECLARATION".equals(name)) return new DexFunctionDeclarationStubElementType(name);
    if ("PACKAGE_CLAUSE".equals(name)) return DexPackageClauseStubElementType.INSTANCE;
    if ("IMPORT_SPEC".equals(name)) return new DexImportSpecStubElementType(name);
    if ("PARAMETERS".equals(name)) return new DexParametersStubElementType(name);
    if ("PARAMETER_DECLARATION".equals(name)) return new DexParameterDeclarationStubElementType(name);
    if ("PARAM_DEFINITION".equals(name)) return new DexParamDefinitionStubElementType(name);
    if ("RESULT".equals(name)) return new DexResultStubElementType(name);
    if ("SIGNATURE".equals(name)) return new DexSignatureStubElementType(name);
    if ("VAR_SPEC".equals(name)) return new DexVarSpecStubElementType(name);
    if ("SHORT_VAR_DECLARATION".equals(name)) return new DexVarSpecStubElementType(name);
    if ("VAR_DEFINITION".equals(name)) return new DexVarDefinitionStubElementType(name);

    Class c = TYPES.get(name);
    if (c != null) {
      return new DexTypeStubElementType(name) {
        @NotNull
        @Override
        public DexType createPsi(@NotNull DexTypeStub stub) {
          try {
            //noinspection unchecked
            return (DexType) ReflectionUtil.createInstance(c.getConstructor(stub.getClass(), IStubElementType.class), stub, this);
          }
          catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
          }
        }
      };
    }
    throw new RuntimeException("Unknown element type: " + name);
  }
}
