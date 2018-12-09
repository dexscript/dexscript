package com.dexscript.runtime.condition;

import com.dexscript.dispatch.ImplEntry;
import com.dexscript.runtime.ImmediateResult;
import com.dexscript.runtime.Promise;
import com.dexscript.runtime.Scheduler;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

import java.util.ArrayList;

public class Add__Int64__Int64 {

    public static final FunctionType FUNCTION_TYPE = new FunctionType(
            "Add__", new ArrayList<Type>() {{
        add(BuiltinTypes.INT64);
        add(BuiltinTypes.INT64);
    }}, BuiltinTypes.INT64, ImplEntry.by(Add__Int64__Int64.class));

    public static Promise call(Scheduler scheduler, Object arg0, Object arg1) {
        return new ImmediateResult(((Long) arg0) + ((Long) arg1));
    }
}
