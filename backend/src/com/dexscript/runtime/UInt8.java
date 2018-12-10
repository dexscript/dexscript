package com.dexscript.runtime;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UInt8 extends Number implements Comparable<UInt8> {

    private final byte val;

    public UInt8(byte val) {
        this.val = val;
    }

    @Override
    public int compareTo(@NotNull UInt8 that) {
        return Byte.toUnsignedInt(val) - Byte.toUnsignedInt(that.val);
    }

    @Override
    public int intValue() {
        return val;
    }

    @Override
    public long longValue() {
        return val;
    }

    @Override
    public float floatValue() {
        return val;
    }

    @Override
    public double doubleValue() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UInt8 uInt8 = (UInt8) o;
        return val == uInt8.val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
