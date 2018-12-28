package com.dexscript.test.framework;


import org.junit.Assert;

import java.lang.reflect.Array;
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

    public static void $(Table table, Function.F2<String, String, Object> sut) {
        for (Row row : table.body) {
            Object obj = sut.apply(row.get(0), row.get(1));
            assertObj(table, row, obj, 2);
        }
    }

    private static void assertObj(Table table, List<String> row, Object obj, int from) {
        int size = table.head.size();
        for (int i = from; i < size; i++) {
            String path = table.head.get(i);
            String expected = row.get(i);
            Object actual = access(obj, trimPath(path));
            String msg = path + ": " + row;
            if (isInteger(expected)) {
                Assert.assertEquals(msg,
                        Long.valueOf(expected),
                        actual == null ? null : ((Number) actual).longValue());
            } else if (isString(expected)) {
                Assert.assertEquals(msg,
                        expected.substring(1, expected.length() - 1),
                        actual == null ? null : actual.toString());
            } else if ("true".equals(expected)) {
                Assert.assertEquals(msg,
                        true,
                        actual);
            } else if ("false".equals(expected)) {
                Assert.assertEquals(msg,
                        false,
                        actual);
            } else if ("null".equals(expected)) {
                Assert.assertNull(msg,
                        actual);
            } else {
                Assert.assertNull(msg,
                        actual);
            }
        }
    }

    private static String trimPath(String path) {
        path = path.trim();
        if (path.startsWith("`")) {
            return path.substring(1, path.length() - 1);
        }
        return path;
    }

    private static Object access(Object obj, String path) {
        if (obj == null) {
            return null;
        }
        if (path.isEmpty()) {
            return obj;
        }
        int next = searchNext(path);
        String pathElem = path;
        if (next != -1) {
            pathElem = path.substring(0, next);
        }
        if (path.charAt(0) == '.') {
            pathElem = pathElem.substring(1);
            obj = accessProperty(obj, path, pathElem);
        } else if (path.charAt(0) == '[') {
            pathElem = pathElem.substring(1, pathElem.length() - 1);
            Integer index = Integer.valueOf(pathElem);
            obj = accessIndex(obj, path, index);
        } else {
            obj = accessProperty(obj, path, pathElem);
        }
        if (next == -1) {
            return obj;
        }
        return access(obj, path.substring(next));
    }

    private static int searchNext(String path) {
        for (int i = 1; i < path.length(); i++) {
            if (path.charAt(i) == '.' || path.charAt(i) == '[') {
                return i;
            }
        }
        return -1;
    }

    private static Object accessIndex(Object obj, String path, Integer index) {
        if (obj instanceof List) {
            List list = (List) obj;
            if (index >= list.size()) {
                return null;
            }
            return list.get(index);
        }
        return Array.get(obj, index);
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
