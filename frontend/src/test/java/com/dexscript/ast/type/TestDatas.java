package com.dexscript.ast.type;

import org.commonmark.node.Node;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dexscript.ast.type.FluentSelectNode.selectSection;

public interface TestDatas {

    static FluentAPI testDataFrom(Class clazz) {
        Node testData = LoadTestData.$(clazz);
        return new FluentAPI(Arrays.asList(testData));
    }

    class FluentAPI {

        private List<Node> nodes;

        public FluentAPI(List<Node> nodes) {
            this.nodes = nodes;
        }

        public FluentAPI select(SelectNode selectNode) {
            nodes = selectNode.select(nodes);
            return this;
        }

        public List<String> text() {
            return new SelectText().select(nodes);
        }

        public void assertMatched(String expectedHeading, Predicate<String> predicate) {
            List<String> texts = select(selectSection(expectedHeading).li()).text();
            for (String text : texts) {
                Assert.assertTrue(text + " should match", predicate.test(text));
            }
        }

        public void assertNotMatched(String expectedHeading, Predicate<String> predicate) {
            List<String> texts = select(selectSection(expectedHeading).li()).text();
            for (String text : texts) {
                Assert.assertFalse(text + " should not match", predicate.test(text));
            }
        }
    }

}
