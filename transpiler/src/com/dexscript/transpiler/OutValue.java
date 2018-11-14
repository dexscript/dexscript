package com.dexscript.transpiler;

import com.dexscript.psi.DexFile;
import com.dexscript.psi.OutCode;

class OutValue extends OutCode {

    public RuntimeType type;

    OutValue(DexFile iFile) {
        super(iFile);
    }
}
