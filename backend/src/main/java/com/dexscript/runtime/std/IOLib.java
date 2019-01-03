package com.dexscript.runtime.std;

import java.io.InputStream;
import java.io.PrintStream;

public class IOLib {

    public static void print(Object object) {
        print(object, System.out);
    }

    public static void print(Object object, PrintStream stream) {
        stream.println(object);
    }

    public static InputStream getSTDIN() {
        return System.in;
    }

    public static PrintStream getSTDOUT() {
        return System.out;
    }

    public static PrintStream getSTDERR() {
        return System.err;
    }
}
