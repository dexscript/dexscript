package com.dexscript.test.framework;

import org.commonmark.node.Node;

import java.util.Arrays;

// entry point, import static
public interface TestFramework {

    static FluentAPI testDataFrom(Class clazz) {
        Node testData = LoadTestData.$(clazz);
        return new FluentAPI(Arrays.asList(testData));
    }

    static FluentSelectNode selectSection(String... expectedHeadings) {
        return new FluentSelectNode().section(expectedHeadings);
    }
}
