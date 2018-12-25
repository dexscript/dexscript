package com.dexscript.test.framework;

import org.commonmark.node.Node;
import org.junit.Assert;

import java.util.List;
import java.util.function.Predicate;

public class FluentAPI {

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
        List<String> texts = select(TestFramework.selectSection(expectedHeading).li()).text();
        for (String text : texts) {
            Assert.assertTrue(text + " should match", predicate.test(text));
        }
    }

    public void assertNotMatched(String expectedHeading, Predicate<String> predicate) {
        List<String> texts = select(TestFramework.selectSection(expectedHeading).li()).text();
        for (String text : texts) {
            Assert.assertFalse(text + " should not match", predicate.test(text));
        }
    }
}
