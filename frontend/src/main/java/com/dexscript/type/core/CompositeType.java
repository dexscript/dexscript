package com.dexscript.type.core;

import java.util.List;

public interface CompositeType extends DType {

    List<FunctionType> functions();
}
