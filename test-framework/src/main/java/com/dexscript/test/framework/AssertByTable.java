package com.dexscript.test.framework;


import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AssertByTable {

    public static void $(Table table, Function.F1<String, Object> sut) {
        for (Row row : table.body) {
            Object obj = sut.apply(row.get(0));
            assertObj(table, row, obj, 1);
        }
    }

    private static void assertObj(Table table, List<String> row, Object obj, int from) {
        int size = table.head.size();
        for (int i = from; i < size; i++) {
            String path = table.head.get(i);
            String expected = row.get(i);
            Object actual = access(obj, path.trim());
            if (isInteger(expected)) {
                Assert.assertEquals(path,
                        Long.valueOf(expected),
                        actual == null ? null : ((Number) actual).longValue());
            } else if (isString(expected)) {
                Assert.assertEquals(path,
                        expected.substring(1, expected.length() - 1),
                        actual == null ? null : actual.toString());
            } else if ("true".equals(expected)) {
                Assert.assertEquals(path,
                        true,
                        actual);
            } else if ("false".equals(expected)) {
                Assert.assertEquals(path,
                        false,
                        actual);
            } else if ("null".equals(expected)) {
                Assert.assertNull(path,
                        actual);
            } else {
                Assert.assertNull(path,
                        actual);
            }
        }
    }

    private static Object access(Object obj, String path) {
        if (path.isEmpty()) {
            return obj;
        }
        return accessProperty(obj, path, path);
    }

    private static Object accessProperty(Object obj, String path, String pathElem) {
        try {
            Method method = obj.getClass().getMethod(pathElem);
            method.setAccessible(true);
            try {
                return method.invoke(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("access: " + path, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("access: " + path, e);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isInteger(String expected) {
        return expected.charAt(0) >= '0' && expected.charAt(0) <= '9';
    }

    private static boolean isString(String expected) {
        return expected.charAt(0) == '"' || expected.charAt(0) == '`';
    }
}
