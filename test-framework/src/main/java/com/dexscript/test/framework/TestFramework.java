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

    static FluentSelectNode selectSection(String... expectedHeadings) {
        return new FluentSelectNode().section(expectedHeadings);
    }

    static void assertMatched(Predicate<String> predicate) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass()).assertMatched(method.getName(), predicate);
    }

    static void assertUnmatched(Predicate<String> predicate) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass()).assertUnmatched(method.getName(), predicate);
    }

    static void assertParsedAST(Function<String, Object> parse) {
        Method method = InspectTestingMethod.$();
        testDataFrom(method.getDeclaringClass()).assertParsedAST(method.getName(), parse);
    }
}
