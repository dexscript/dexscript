package com.dexscript.type.core;

public interface FunctionImplProvider {
    // for generic function, the function will be expanded
    // for each expanded function, it need a impl
    Object implOf(FunctionType expandedFunc);
}
