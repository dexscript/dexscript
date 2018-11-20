package com.dexscript.parser2;

import com.dexscript.parser2.core.Expect;
import com.dexscript.parser2.core.Text;

import java.lang.reflect.Method;

public class DexError {

    public final Text src;
    public final int errorPos;
    public final StackTraceElement[] frames;

    public DexError(Text src, int errorPos) {
        this.src = src;
        this.errorPos = errorPos;
        frames = Thread.currentThread().getStackTrace();
    }

    @Override
    public String toString() {
        int errorLeft = errorPos - 16;
        if (errorLeft < src.begin) {
            errorLeft = src.begin;
        }
        int errorRight = errorPos + 16;
        if (errorRight > src.end) {
            errorRight = src.end;
        }
        byte[] contextBytes = new byte[errorRight - errorLeft + 6];
        int j = 0;
        for (int i = errorLeft; i < errorRight; i++) {
            if (i == errorPos) {
                contextBytes[j++] = '>';
                contextBytes[j++] = '>';
                contextBytes[j++] = '>';
            }
            contextBytes[j++] = src.bytes[i];
            if (i == errorPos) {
                contextBytes[j++] = '<';
                contextBytes[j++] = '<';
                contextBytes[j++] = '<';
            }
        }
        StringBuilder msg = new StringBuilder();
        msg.append("found error around >>>|");
        msg.append(new String(contextBytes));
        msg.append("|<<<\n");
        for (StackTraceElement frame : frames) {
            try {
                describeExpectation(msg, frame);
            } catch (Exception e) {

            }
        }
        return msg.toString();
    }

    private void describeExpectation(StringBuilder msg, StackTraceElement frame) throws Exception {
        Class<?> clazz = Class.forName(frame.getClassName());
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getName().equals(frame.getMethodName())) {
                continue;
            }
            Expect[] expects = null;
            Expect.List expectList = method.getAnnotation(Expect.List.class);
            if (expectList != null) {
                expectList.value();
            }
            if (method.getAnnotation(Expect.class) != null) {
                expects = new Expect[] { method.getAnnotation(Expect.class) };
            }
            if (expects == null) {
                continue;
            }
            for (Expect expect : expects) {
                msg.append("expect ");
                msg.append(expect.value());
                msg.append(" @ ");
                msg.append(frame.toString());
                msg.append("\n");
            }
        }
    }
}
