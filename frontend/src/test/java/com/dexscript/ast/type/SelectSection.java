package com.dexscript.ast.type;

import org.commonmark.node.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectSection implements SelectNode {

    private final String[] expectedHeadings;

    public SelectSection(String... expectedHeadings) {
        this.expectedHeadings = expectedHeadings;
    }

    @Override
    public List<Node> select(List<Node> nodes) {
        List<Node> selected = new ArrayList<>();
        List<String> headings = new ArrayList<>();
        AbstractVisitor visitor = new AbstractVisitor() {

            private boolean shouldSelect;

            @Override
            public void visit(BulletList bulletList) {
                if (shouldSelect) {
                    selected.add(bulletList);
                }
            }

            @Override
            public void visit(Paragraph paragraph) {
                if (shouldSelect) {
                    selected.add(paragraph);
                }
            }

            @Override
            public void visit(Heading heading) {
                if (heading.getLevel() == headings.size() + 1) {
                    headings.add(getText(heading));
                    shouldSelect = shouldSelect(headings);
                    return;
                }
                List<String> newHeadings = new ArrayList<>(headings.subList(0, heading.getLevel() - 1));
                newHeadings.add(getText(heading));
                headings.clear();
                headings.addAll(newHeadings);
                shouldSelect = shouldSelect(headings);
            }
        };
        for (Node node : nodes) {
            node.accept(visitor);
        }
        return selected;
    }

    private boolean shouldSelect(List<String> headings) {
        if (headings.size() != expectedHeadings.length) {
            return false;
        }
        for (int i = 0; i < expectedHeadings.length; i++) {
            String expectedHeading = expectedHeadings[i];
            if (!expectedHeading.equals(headings.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static String getText(Heading heading) {
        List<String> texts = new SelectText().select(Arrays.asList(heading));
        if (texts.size() == 1) {
            return texts.get(0);
        }
        StringBuilder concat = new StringBuilder();
        for (String text : texts) {
            concat.append(text);
        }
        return concat.toString();
    }
}
