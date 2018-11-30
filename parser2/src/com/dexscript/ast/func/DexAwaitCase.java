package com.dexscript.ast.func;

import com.dexscript.ast.core.Text;

public abstract class DexAwaitCase extends DexStatement {

    public DexAwaitCase(Text src) {
        super(src);
    }

    public static DexAwaitCase parse(Text src) {
        return new DexAwaitConsumerStmt(src);
    }
}
