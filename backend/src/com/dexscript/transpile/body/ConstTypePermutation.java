package com.dexscript.transpile.body;

import com.dexscript.type.DType;

import java.util.Iterator;
import java.util.List;

class ConstTypePermutation implements Iterator<List<DType>> {

    private final List<DType> orig;

    ConstTypePermutation(List<DType> orig) {
        this.orig = orig;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public List<DType> next() {
        return null;
    }
}
