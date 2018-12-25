package com.dexscript.test.framework;

import java.lang.reflect.Method;

public interface InspectTestingMethod {

    static Method $() {
        try {
            StackTraceElement[] frames = Thread.currentThread().getStackTrace();
            for (StackTraceElement frame : frames) {
                Class<?> clazz = Class.forName(frame.getClassName());
                try {
                    Method method = clazz.getMethod(frame.getMethodName());
                    if (method.getAnnotation(org.junit.Test.class) != null) {
                        return method;
                    }
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("can not find test method in stack trace");
    }
}
