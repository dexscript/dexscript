package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

public class Invoked {

    // const type will be substituted,
    // so the invocation args may not be the same as invoked args
    public final List<DType> args;

    // last candidate must be a match
    // other candidates are possible match, need runtime check
    public final List<FunctionSig.Invoked> candidates = new ArrayList<>();
    // some function is incompatible with the invocation
    public final List<FunctionSig.Invoked> failures = new ArrayList<>();
    // some function maybe compatible with the invocation
    // but ignored due to not exact override of the match
    public final List<FunctionSig.Invoked> ignoreds = new ArrayList<>();
    // if match found, other functions will be skipped
    public final List<FunctionType> skippeds = new ArrayList<>();

    public Invoked(List<DType> args) {
        this.args = args;
    }
}
