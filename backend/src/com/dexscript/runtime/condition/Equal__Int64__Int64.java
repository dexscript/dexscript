package com.dexscript.runtime.condition;

import com.dexscript.dispatch.ImplEntry;
import com.dexscript.runtime.ImmediateResult;
import com.dexscript.runtime.Promise;
import com.dexscript.runtime.Scheduler;
import com.dexscript.type.BuiltinTypes;
import com.dexscript.type.FunctionType;
import com.dexscript.type.Type;

import java.util.ArrayList;

public class Equal__Int64__Int64 {

    public static final FunctionType FUNCTION_TYPE = new FunctionType(
            "Equal__", new ArrayList<Type>() {{
        add(BuiltinTypes.INT64);
        add(BuiltinTypes.INT64);
    }}, BuiltinTypes.BOOL, ImplEntry.by(Equal__Int64__Int64.class));

    public static Promise call(Scheduler scheduler, Object arg0, Object arg1) {
        return new ImmediateResult(arg0.equals(arg1));
    }
}
