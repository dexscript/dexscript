package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

public class Invoked {

    // const type will be substituted,
    // so the invocation args may not be the same as invoked args
    public List<DType> args;

    // the return type of all candidates union together
    public DType ret;

    // map named arg => positional arg
    public int[] namedArgsMapping;

    // signature matched
    public FunctionSig.Invoked match;

    // last candidate may not be the match, if match is interface function without impl
    // other candidates are possible match, need runtime check
    public final List<FunctionSig.Invoked> candidates = new ArrayList<>();

    // some function is incompatible with the invocation
    public final List<FunctionSig.Invoked> failures = new ArrayList<>();

    // some function maybe compatible with the invocation
    // but ignored due to not exact override of the match
    public final List<FunctionSig.Invoked> ignoreds = new ArrayList<>();

    // if match found, other functions will be skipped
    public final List<FunctionType> skippeds = new ArrayList<>();
}
