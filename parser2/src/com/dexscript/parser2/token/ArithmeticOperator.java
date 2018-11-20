package com.dexscript.parser2.token;

public class ArithmeticOperator {
    public static boolean __(byte b) {
        return (b == '+') || (b == '-') || (b == '*') || (b == '/');
    }
}
