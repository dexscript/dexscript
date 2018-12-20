package com.dexscript.type;

import java.util.List;

public class Invoked {

    private final List<DType> args;
    private final List<FunctionSig.Invoked> successes;
    private final List<FunctionSig.Invoked> failures;
    private final List<FunctionType> ignoreds;

    public Invoked(List<DType> args, List<FunctionSig.Invoked> successes, List<FunctionSig.Invoked> failures, List<FunctionType> ignoreds) {
        this.args = args;
        this.successes = successes;
        this.failures = failures;
        this.ignoreds = ignoreds;
    }

    public List<DType> args() {
        return args;
    }

    public List<FunctionSig.Invoked> successes() {
        return successes;
    }

    public List<FunctionSig.Invoked> failures() {
        return failures;
    }

    public List<FunctionType> ignoreds() {
        return ignoreds;
    }
}
