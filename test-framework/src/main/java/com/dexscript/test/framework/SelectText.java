package com.dexscript.test.framework;

import org.commonmark.node.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectText {

    public List<String> select(List<Node> nodes) {
        List<String> selected = new ArrayList<>();
        Visitor visitor = new AbstractVisitor() {
            @Override
            public void visit(Text text) {
                selected.add(text.getLiteral());
            }

            @Override
            public void visit(Code text) {
                selected.add('`' + text.getLiteral() + '`');
            }

            @Override
            public void visit(BulletList bulletList) {
            }
        };
        for (Node node : nodes) {
            node.accept(visitor);
        }
        return selected;
    }

    public static String getText(Node node) {
        List<String> texts = new SelectText().select(Arrays.asList(node));
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
