package com.dexscript.runtime.std;

import java.util.Objects;

public class ComparisonLib {

    public static boolean Equal__(Object left, Object right) {
        return Objects.equals(left, right);
    }

    public static boolean LessThan__(long left, long right) {
        return left < right;
    }

    public static boolean GreaterThan__(long left, long right) { return left > right; }
}
