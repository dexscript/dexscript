package com.dexscript.gen;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class Join {

    public final String separator;
    public final Collection objs;
    public final Function<Object, String> printer;

    public <T> Join(String separator, T[] objs, Function<T, String> printer) {
        this.separator = separator;
        this.objs = Arrays.asList(objs);
        this.printer = (Function<Object, String>) printer;
    }

    public <T> Join(String separator, Collection<T> objs, Function<T, String> printer) {
        this.separator = separator;
        this.objs = objs;
        this.printer = (Function<Object, String>) printer;
    }
}
