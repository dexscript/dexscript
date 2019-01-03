package com.dexscript.runtime.std;

import java.io.InputStream;
import java.io.PrintStream;

public class IOLib {

    public final static Stdio STDIO = new Stdio();

    public static void print(Object object) {
        print(object, System.out);
    }

    public static void print(Object object, PrintStream stream) {
        stream.println(object);
    }

    public static Stdio stdio() {
        return STDIO;
    }

    public static class Stdio {
        public PrintStream err() {
            return System.err;
        }

        public PrintStream out() {
            return System.out;
        }

        public InputStream in() {
            return System.in;
        }
    }
}
