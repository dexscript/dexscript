package com.dexscript.type;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IsAssignable {

    private final DType to;
    private final DType from;
    private final Set<FunctionType> available = new HashSet<>();

    public static boolean $(DType to, DType from) {
        return new IsAssignable(to, from).result();
    }

    public void makeAvailable(FunctionType function) {
        available.add(function);
    }

    public boolean isAvailable(FunctionType function) {
        if (available.contains(function)) {
            return true;
        }
        return function.impl() != null;
    }

    public void dump() {
        dump("");
    }

    public void dump(String prefix) {
        if (result) {
            System.out.println(prefix + ">>> " + to + " is assignable from " + from);
        } else {
            System.out.println(prefix + ">>> " + to + " is not assignable from " + from);
        }
        for (Log log : logs) {
            System.out.println(prefix + log.msg);
            if (log.details != null) {
                log.details.dump(prefix + "  ");
            }
        }
        System.out.println(prefix + "<<<");
    }

    public static class Log {

        public final String msg;

        public final IsAssignable details;

        public Log(String msg) {
            this.msg = msg;
            this.details = null;
        }

        public Log(String msg, IsAssignable details) {
            this.msg = msg;
            this.details = details;
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    private boolean result;
    private final List<Log> logs = new ArrayList<>();
    private final Set<TypeComparison> currentTypeComparisons;

    public IsAssignable(DType to, DType from) {
        this.to = to;
        this.from = from;
        currentTypeComparisons = new HashSet<>();
        result = to._isSubType(this, from);
    }

    public IsAssignable(IsAssignable parent, String comparing, DType to, DType from) {
        this.to = to;
        this.from = from;
        currentTypeComparisons = parent.currentTypeComparisons;
        TypeComparison typeComparison = new TypeComparison(to, from);
        if (currentTypeComparisons.contains(typeComparison)) {
            result = true;
        } else {
            currentTypeComparisons.add(typeComparison);
            result = to._isSubType(this, from);
            currentTypeComparisons.remove(typeComparison);
        }
        parent.addLog(comparing, this);
    }

    public DType to() {
        return to;
    }

    public DType from() {
        return from;
    }

    public boolean result() {
        return result;
    }

    public void addLog(String comparing, IsAssignable details) {
        String msg = comparing + " is " + (details.result() ? "assignable" : "not assignable");
        logs.add(new Log(msg, details));
    }

    public void addLog(String prefix, Object... args) {
        StringBuilder msg = new StringBuilder(prefix);
        msg.append(':');
        for (int i = 0; i < args.length; i += 2) {
            msg.append(' ');
            String argName = (String) args[i];
            String argValue = args[i + 1].toString();
            msg.append(argName);
            msg.append("=");
            msg.append(argValue);
        }
        logs.add(new Log(msg.toString()));
    }

    public List<Log> logs() {
        return logs;
    }
}
