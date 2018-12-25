package com.dexscript.test.framework;

import org.commonmark.node.Node;
import org.commonmark.node.Visitor;
import org.junit.Assert;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dexscript.test.framework.TestFramework.selectSection;

public class FluentAPI {

    private List<Node> nodes;

    public FluentAPI(List<Node> nodes) {
        this.nodes = nodes;
    }

    public FluentAPI select(SelectNode selectNode) {
        return new FluentAPI(selectNode.select(nodes));
    }

    public List<String> text() {
        return new SelectText().select(nodes);
    }

    public List<String> code() {
        return new SelectCode().select(nodes);
    }

    public void assertMatched(String expectedHeading, Predicate<String> predicate) {
        List<String> texts = select(selectSection(expectedHeading).li()).text();
        if (texts.isEmpty()) {
            throw new RuntimeException("no text found in section: " + expectedHeading);
        }
        for (String text : texts) {
            Assert.assertTrue(text + " should match", predicate.test(text));
        }
    }

    public void assertNotMatched(String expectedHeading, Predicate<String> predicate) {
        List<String> texts = select(selectSection(expectedHeading).li()).text();
        if (texts.isEmpty()) {
            throw new RuntimeException("no text found in section: " + expectedHeading);
        }
        for (String text : texts) {
            Assert.assertFalse(text + " should not match", predicate.test(text));
        }
    }

    public void assertParsedAST(String expectedHeading, Function<String, Object> parse) {
        FluentAPI selected = select(selectSection(expectedHeading));
        List<String> codes = selected.code();
        if (codes.isEmpty()) {
            Assert.fail("no code found in section: " + expectedHeading);
        }
        String code = stripCode(codes.get(0));
        Object obj = parse.apply(code);
        String expectedToString = code;
        if (codes.size() >= 2) {
            expectedToString = stripCode(codes.get(1));
        }
        Assert.assertEquals(expectedToString, obj.toString());
        Visitor visitor = new AssertObject(obj);
        for (Node node : selected.nodes) {
            node.accept(visitor);
        }
    }

    private String stripCode(String code) {
        if (code.charAt(code.length() - 1) != '\n') {
            throw new RuntimeException("code not ends with newline");
        }
        return code.substring(0, code.length() - 1);
    }

}
