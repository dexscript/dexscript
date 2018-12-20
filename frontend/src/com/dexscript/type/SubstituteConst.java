package com.dexscript.type;

import java.util.ArrayList;
import java.util.Arrays;
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
            columns.add(new Column(widen(type.typeSystem(), type)));
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

    private static List<DType> widen(TypeSystem ts, DType type) {
        if (type instanceof BoolConstType) {
            String val = ((BoolConstType) type).constValue();
            return Arrays.asList(ts.literalOfBool(val), ts.BOOL);
        }
        if (type instanceof StringConstType) {
            String val = ((StringConstType) type).constValue();
            return Arrays.asList(ts.literalOf(val), ts.STRING);
        }
        if (type instanceof IntegerConstType) {
            String val = ((IntegerConstType) type).constValue();
            return Arrays.asList(ts.literalOfInteger(val), ts.INT64, ts.INT32, ts.FLOAT64, ts.FLOAT32);
        }
        if (type instanceof FloatConstType) {
            return Arrays.asList(ts.FLOAT64, ts.FLOAT32);
        }
        return Arrays.asList(type);
    }
}
