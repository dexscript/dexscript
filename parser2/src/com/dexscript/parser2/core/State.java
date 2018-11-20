package com.dexscript.parser2.core;

public interface State {

    State handle();

    static void Play(State state) {
        while (state != null) {
            state = state.handle();
        }
    }
}
