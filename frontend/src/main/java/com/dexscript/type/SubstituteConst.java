package com.dexscript.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class SubstituteConst implements Iterator<SubstituteConst.Combination> {

    public static class Combination {

        public List<DType> posArgs = new ArrayList<>();
        public List<NamedArg> namedArgs = new ArrayList<>();
    }

    interface Column {

        boolean next();

        void collect(Combination collected);
    }

    static class PosColumn implements Column {

        int cursor;

        List<DType> candidates;

        PosColumn(List<DType> candidates) {
            this.candidates = candidates;
        }

        public boolean next() {
            cursor += 1;
            if (cursor >= candidates.size()) {
                cursor = 0;
                return false;
            }
            return true;
        }

        @Override
        public void collect(Combination collected) {
            collected.posArgs.add(candidates.get(cursor));
        }
    }

    static class NamedColumn implements Column {

        int cursor;
        String name;
        List<DType> candidates;

        NamedColumn(String name, List<DType> candidates) {
            this.name = name;
            this.candidates = candidates;
        }

        public boolean next() {
            cursor += 1;
            if (cursor >= candidates.size()) {
                cursor = 0;
                return false;
            }
            return true;
        }

        @Override
        public void collect(Combination collected) {
            collected.namedArgs.add(new NamedArg(name, candidates.get(cursor)));
        }
    }

    private Combination current;
    private List<Column> columns = new ArrayList<>();

    SubstituteConst(List<DType> posArgs, List<NamedArg> namedArgs) {
        for (DType posArg : posArgs) {
            columns.add(new PosColumn(widen(posArg.typeSystem(), posArg)));
        }
        for (NamedArg namedArg : namedArgs) {
            List<DType> candidates = widen(namedArg.type().typeSystem(), namedArg.type());
            columns.add(new NamedColumn(namedArg.name(), candidates));
        }
        current = collect();
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Combination next() {
        Combination next = null;
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

    private Combination collect() {
        Combination collected = new Combination();
        for (Column column : columns) {
            column.collect(collected);
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
