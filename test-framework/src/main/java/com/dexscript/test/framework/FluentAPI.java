package com.dexscript.test.framework;

import org.commonmark.node.Node;
import org.commonmark.node.Visitor;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class FluentAPI {

    private List<Node> nodes;

    public FluentAPI(List<Node> nodes) {
        this.nodes = nodes;
    }

    public FluentAPI select(SelectNode selectNode) {
        return new FluentAPI(selectNode.select(nodes));
    }

    public List<String> texts() {
        return new SelectText().select(nodes);
    }

    public List<String> codes() {
        return new SelectCode().select(nodes);
    }

    public List<Table> tables() {
        return new SelectTable().select(nodes);
    }

    public Table table() {
        List<Table> tables = tables();
        if (tables.isEmpty()) {
            Assert.fail("no table found");
        }
        return tables.get(0);
    }

    public String code() {
        List<String> codes = codes();
        if (codes.isEmpty()) {
            Assert.fail("no code found");
        }
        return stripCode(codes.get(0));
    }

    public void assertTrue(Function.F1<String, Boolean> sut) {
        List<String> texts = select(new SelectListItem()).texts();
        if (texts.isEmpty()) {
            throw new RuntimeException("no text found");
        }
        for (String text : texts) {
            text = translateDoubleQuote(text);
            Assert.assertTrue(text + " should be true", sut.apply(text));
        }
    }

    public void assertFalse(Function.F1<String, Boolean> sut) {
        List<String> texts = select(new SelectListItem()).texts();
        if (texts.isEmpty()) {
            throw new RuntimeException("no text found");
        }
        for (String text : texts) {
            text = translateDoubleQuote(text);
            Assert.assertFalse(text + " should be false", sut.apply(text));
        }
    }

    public void assertByList(Function.F1<String, Object> sut) {
        List<String> codes = this.codes();
        if (codes.isEmpty()) {
            Assert.fail("no code found");
        }
        String code = stripCode(codes.get(0));
        Object obj = sut.apply(code);
        String expectedToString = code;
        if (codes.size() >= 2) {
            expectedToString = stripCode(codes.get(1));
        }
        Assert.assertEquals(expectedToString, obj.toString());
        Visitor visitor = new AssertByList(obj);
        for (Node node : this.nodes) {
            node.accept(visitor);
        }
    }

    public void assertByTable(Function.F1<String, Object> sut) {
        Table table = this.table();
        AssertByTable.$(table, sut);
    }

    private String translateDoubleQuote(String text) {
        if (text.isEmpty()) {
            return text;
        }
        if (text.charAt(0) != '"') {
            return text;
        }
        return text.substring(1, text.length() - 1)
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }

    private String stripCode(String code) {
        if (code.charAt(code.length() - 1) != '\n') {
            throw new RuntimeException("code not ends with newline");
        }
        return code.substring(0, code.length() - 1);
    }
}
