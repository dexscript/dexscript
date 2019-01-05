package com.dexscript.type;

import java.util.List;

public interface CompositeType extends DType {

    List<FunctionType> functions();
}
