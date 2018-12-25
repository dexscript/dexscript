package com.dexscript.test.framework;

import org.commonmark.node.Node;

import java.util.ArrayList;
import java.util.List;

public class FluentSelectNode implements SelectNode {

    private final List<SelectNode> selectors = new ArrayList<>();

    public FluentSelectNode section(String... expectedHeadings) {
        selectors.add(new SelectSection(expectedHeadings));
        return this;
    }

    public FluentSelectNode li() {
        selectors.add(new SelectListItem());
        return this;
    }

    @Override
    public List<Node> select(List<Node> nodes) {
        for (SelectNode selector : selectors) {
            nodes = selector.select(nodes);
        }
        return nodes;
    }
}
