package com.dexscript.ast.type;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.node.Visitor;

import java.util.ArrayList;
import java.util.List;

public class SelectText {

    public List<String> select(List<Node> nodes) {
        List<String> selected = new ArrayList<>();
        Visitor visitor = new AbstractVisitor() {
            @Override
            public void visit(Text text) {
                selected.add(text.getLiteral());
            }
        };
        for (Node node : nodes) {
            node.accept(visitor);
        }
        return selected;
    }
}
