package com.dexscript.test.framework;

import org.commonmark.node.Node;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

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

    static void assertTrue(Predicate<String> predicate) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass())
                .select(selectSection(method.getName()))
                .assertTrue(predicate);
    }

    static void assertFalse(Predicate<String> predicate) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass())
                .select(selectSection(method.getName()))
                .assertFalse(predicate);
    }

    static void assertObject(Function<String, Object> parse) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass())
                .select(selectSection(method.getName()))
                .assertObject(parse);
    }
}
