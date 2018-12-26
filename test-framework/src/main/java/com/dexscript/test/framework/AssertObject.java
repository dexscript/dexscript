package com.dexscript.test.framework;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.ListItem;
import org.junit.Assert;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

import static com.dexscript.test.framework.SelectText.getText;

class AssertObject extends AbstractVisitor {

    private Object current;
    private final Stack<String> path = new Stack<>();

    public AssertObject(Object obj) {
        current = obj;
    }

    @Override
    public void visit(ListItem listItem) {
        String pathElem = getText(listItem);
        if (pathElem.length() == 0) {
            throw new RuntimeException("no text at path " + path);
        }
        path.add(pathElem);
        Object old = current;
        try {
            if (isInteger(pathElem)) {
                Assert.assertEquals("path " + path,
                        Long.valueOf(pathElem),
                        current == null ? null : ((Number) current).longValue());
            } else if (isString(pathElem)) {
                Assert.assertEquals("path " + path,
                        pathElem.substring(1, pathElem.length() - 1),
                        current == null ? null : current.toString());
            } else if ("true".equals(pathElem)) {
                Assert.assertEquals("path " + path,
                        true,
                        current);
            } else if ("false".equals(pathElem)) {
                Assert.assertEquals("path " + path,
                        false,
                        current);
            } else if (isIndex(pathElem)) {
                enterIndex(pathElem);
                super.visit(listItem);
            } else {
                enterProperty(pathElem);
                super.visit(listItem);
            }
        } finally {
            current = old;
            path.pop();
        }
    }

    private void enterIndex(String pathElem) {
        pathElem = pathElem.substring(1, pathElem.length() - 1);
        if (isInteger(pathElem)) {
            int i = Integer.valueOf(pathElem);
            if (current instanceof List) {
                current = ((List) current).get(i);
                return;
            }
            current = Array.get(current, i);
        } else {
            throw new RuntimeException("not valid path elem: " + pathElem);
        }
    }

    private static boolean isIndex(String pathElem) {
        return pathElem.charAt(0) == '[';
    }

    private static boolean isInteger(String pathElem) {
        return pathElem.charAt(0) >= '0' && pathElem.charAt(0) <= '9';
    }

    private static boolean isString(String pathElem) {
        return pathElem.charAt(0) == '"' || pathElem.charAt(0) == '`';
    }

    private void enterProperty(String pathElem) {
        try {
            Method method = current.getClass().getMethod(pathElem);
            method.setAccessible(true);
            try {
                current = method.invoke(current);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("access: " + path, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("access: " + path, e);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
