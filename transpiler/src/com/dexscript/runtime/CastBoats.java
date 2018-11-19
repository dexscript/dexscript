package com.dexscript.runtime;

import com.dexscript.transpiler.Boat;
import com.dexscript.transpiler.OutShim;
import com.dexscript.transpiler.Pier;

public class CastBoats {

    public static void init(OutShim oShim) {
        Pier cast2 = new Pier("Cast__", 2);
        String className = CastBoats.class.getCanonicalName();
        oShim.addBoat(new Boat(cast2, className, "castLongToInt"));
    }


    public static boolean can__castLongToInt(Object castFrom, Object castToType) {
        return castFrom.getClass() == Long.class && castToType == Integer.class;
    }

    public static Result castLongToInt(Object castFrom, Object castToType) {
        return new Result1Impl(((Long)castFrom).intValue());
    }
}
