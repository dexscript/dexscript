package com.dexscript.test.framework;

public interface Function {

    interface F1<A0, R> {
        R apply(A0 arg0);
    }
    
    interface F2<A0, A1, R> {
        R apply(A0 arg0, A1 arg1);
    }
}
