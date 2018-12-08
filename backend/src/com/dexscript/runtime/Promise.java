package com.dexscript.runtime;

public interface Promise {

    boolean finished();

    Object value();
}
