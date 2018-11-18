package com.dexscript.runtime;

import com.dexscript.transpiler.Boat;
import com.dexscript.transpiler.OutShim;
import com.dexscript.transpiler.Pier;

public class AddBoats {

    public static void init(OutShim oShim) {
        Pier add2 = new Pier("Add__", 2);
        String className = AddBoats.class.getCanonicalName();
        oShim.addBoat(new Boat(add2, className, "addLong"));
        oShim.addBoat(new Boat(add2, className, "addInt"));
        oShim.addBoat(new Boat(add2, className, "addString"));
    }

    public static Result addLong(Object left, Object right) {
        return new Result1Impl((Long) left + (Long) right);
    }

    public static boolean can__addLong(Object left, Object right) {
        return left.getClass() == Long.class && right.getClass() == Long.class;
    }

    public static Result addInt(Object left, Object right) {
        return new Result1Impl((Integer) left + (Integer) right);
    }

    public static boolean can__addInt(Object left, Object right) {
        return left.getClass() == Integer.class && right.getClass() == Integer.class;
    }

    public static Result addString(Object left, Object right) {
        return new Result1Impl((String) left + (String) right);
    }

    public static boolean can__addString(Object left, Object right) {
        return left.getClass() == String.class && right.getClass() == String.class;
    }
}
