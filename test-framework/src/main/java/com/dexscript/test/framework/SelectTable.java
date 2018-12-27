package com.dexscript.test.framework;

import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TableCell;
import org.commonmark.ext.gfm.tables.TableHead;
import org.commonmark.ext.gfm.tables.TableRow;
import org.commonmark.node.*;

import java.util.ArrayList;
import java.util.List;

public class SelectTable {

    public List<Table> select(List<Node> nodes) {
        List<Table> tables = new ArrayList<>();
        Visitor visitor = new AbstractVisitor() {
            @Override
            public void visit(CustomBlock customBlock) {
                if (!(customBlock instanceof TableBlock)) {
                    return;
                }
                Table table = new Table();
                tables.add(table);
                new FillTable(table, (TableBlock) customBlock);
            }
        };
        for (Node node : nodes) {
            node.accept(visitor);
        }
        return tables;
    }

    private static class FillTable extends AbstractVisitor {

        private final Table table;

        private FillTable(Table table, TableBlock blk) {
            this.table = table;
            visitChildren(blk);
        }

        @Override
        public void visit(CustomNode customNode) {
            if (customNode instanceof TableHead) {
                Row head = new Row();
                table.head = head;
                new FillRow(head, customNode);
            } else if (customNode instanceof TableRow) {
                Row row = new Row();
                table.body.add(row);
                new FillRow(row, customNode);
            } else {
                super.visit(customNode);
            }
        }
    }

    private static class FillRow extends AbstractVisitor {

        private Row row;

        public FillRow(Row row, CustomNode node) {
            this.row = row;
            visitChildren(node);
        }

        @Override
        public void visit(CustomNode customNode) {
            if (customNode instanceof TableCell) {
                row.add(SelectText.getText(customNode));
            } else {
                super.visit(customNode);
            }
        }
    }
}
