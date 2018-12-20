package com.dexscript.transpile.body;

import com.dexscript.type.DType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class SubstituteConst implements Iterator<List<DType>> {

    private static class Column {

        int cursor;

        List<DType> candidates;

        public Column(List<DType> candidates) {
            this.candidates = candidates;
        }

        public DType current() {
            return candidates.get(cursor);
        }

        public boolean next() {
            cursor += 1;
            if (cursor >= candidates.size()) {
                cursor = 0;
                return false;
            }
            return true;
        }
    }

    private List<DType> current;
    private List<Column> columns = new ArrayList<>();

    SubstituteConst(List<DType> orig) {
        for (DType type : orig) {
            columns.add(new Column(type.typeSystem().widen(type)));
        }
        current = collect();
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public List<DType> next() {
        List<DType> next = null;
        for (Column column : columns) {
            if (column.next()) {
                next = collect();
                break;
            }
        }
        try {
            return current;
        } finally {
            current = next;
        }
    }

    private List<DType> collect() {
        List<DType> collected = new ArrayList<>();
        for (Column column : columns) {
            collected.add(column.current());
        }
        return collected;
    }
}
