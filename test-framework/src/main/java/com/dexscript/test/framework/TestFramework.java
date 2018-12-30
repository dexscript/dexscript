package com.dexscript.test.framework;

import org.commonmark.node.Node;

import java.lang.reflect.Method;
import java.util.Arrays;

// entry point, import static
public interface TestFramework {

    static FluentAPI testDataFrom(Class clazz) {
        Node testData = LoadTestData.$(clazz);
        return new FluentAPI(Arrays.asList(testData));
    }

    static FluentAPI testDataFromMySection() {
        Method method = InspectTestingMethod.$();
        return testDataFrom(method.getDeclaringClass())
                .select(selectSection(method.getName()));
    }

    static FluentSelectNode selectSection(String... expectedHeadings) {
        return new FluentSelectNode().section(expectedHeadings);
    }

    static void assertTrue(Function.F1<String, Boolean> sut) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass())
                .select(selectSection(method.getName()))
                .assertTrue(sut);
    }

    static void assertFalse(Function.F1<String, Boolean> sut) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass())
                .select(selectSection(method.getName()))
                .assertFalse(sut);
    }

    static void assertObject(Function.F1<String, Object> sut) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass())
                .select(selectSection(method.getName()))
                .assertByList(sut);
    }

    static String stripQuote(String text) {
        if (text.isEmpty()) {
            return text;
        }
        if (text.charAt(0) == '`') {
            return text.substring(1, text.length() - 1).replace("\\|", "|");
        }
        return text;
    }
}
