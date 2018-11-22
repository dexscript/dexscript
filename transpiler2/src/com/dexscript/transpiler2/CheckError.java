package com.dexscript.transpiler2;

import com.dexscript.parser2.DexFile;
import com.dexscript.parser2.DexTraversal;
import com.dexscript.parser2.core.DexElement;

public class CheckError {

    private boolean result = false;

    public CheckError(DexFile file) {
        DexTraversal.__(this::traverse, file);
    }

    private void traverse(DexElement elem) {
        if (elem.err() != null) {
            System.out.println(elem);
            result = true;
        }
    }

    public boolean result() {
        return result;
    }
}
