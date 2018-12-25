package com.dexscript.test.framework;

import org.commonmark.node.*;

import java.util.ArrayList;
import java.util.List;

public class SelectListItem implements SelectNode {

    @Override
    public List<Node> select(List<Node> nodes) {
        List<Node> selected = new ArrayList<>();
        Visitor visitor = new AbstractVisitor() {
            @Override
            public void visit(ListItem listItem) {
                selected.add(listItem);
            }
        };
        for (Node node : nodes) {
            node.accept(visitor);
        }
        return selected;
    }
}
