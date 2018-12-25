package com.dexscript.test.framework;

import org.commonmark.node.*;

import java.util.ArrayList;
import java.util.List;

public class SelectCode {

    public List<String> select(List<Node> nodes) {
        List<String> selected = new ArrayList<>();
        Visitor visitor = new AbstractVisitor() {
            @Override
            public void visit(FencedCodeBlock code) {
                selected.add(code.getLiteral());
            }
        };
        for (Node node : nodes) {
            node.accept(visitor);
        }
        return selected;
    }
}
