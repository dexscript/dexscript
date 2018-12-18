package com.dexscript.type;


import java.util.*;

public class IsAssignable {

    private boolean result;
    private final List<Log> logs = new ArrayList<>();
    private final Set<TypeComparison> currentTypeComparisons;
    private final DType to;
    private final DType from;
    private final Map<DType, DType> tempSub;
    private final Set<FunctionType> available = new HashSet<>();

    public IsAssignable(Map<DType, DType> permSub) {
        this.to = null;
        this.from = null;
        this.tempSub = permSub;
        this.currentTypeComparisons = new HashSet<>();
    }

    public IsAssignable(DType to, DType from) {
        this.to = to;
        this.from = from;
        this.tempSub = new HashMap<>();
        this.currentTypeComparisons = new HashSet<>();
        result = isAssignableWithCache(this, new TypeComparison(to, from));
    }

    public IsAssignable(IsAssignable parent, String comparing, DType to, DType from) {
        this.tempSub = new HashMap<>(parent.tempSub);
        DType substituted = tempSub.get(to);
        if (substituted != null) {
            addLog("substituted", "placeholder", to, "substituted", substituted);
            to = substituted;
        }
        this.to = to;
        this.from = from;
        this.currentTypeComparisons = parent.currentTypeComparisons;
        TypeComparison typeComparison = new TypeComparison(to, from);
        if (currentTypeComparisons.contains(typeComparison)) {
            result = true;
        } else {
            currentTypeComparisons.add(typeComparison);
            result = isAssignable(this, to, from);
            currentTypeComparisons.remove(typeComparison);
        }
        parent.addLog(comparing, this);
        if (result) {
            parent.tempSub.putAll(tempSub);
        }
    }

    private static boolean isAssignableWithCache(IsAssignable ctx, TypeComparison comparison) {
        DType to = comparison.to();
        DType from = comparison.from();
        TypeSystem ts = to.typeSystem();
        TypeComparisonCache typeComparisonCache = ts.comparisonCache();
        Boolean cached = typeComparisonCache.get(comparison);
        if (cached != null) {
            return cached;
        }
        cached = isAssignable(ctx, to, from);
        typeComparisonCache.set(comparison, cached);
        return cached;
    }

    private static boolean isAssignable(IsAssignable ctx, DType to, DType from) {
        if (from instanceof IntersectionType) {
            for (DType member : ((IntersectionType) from).members()) {
                if (new IsAssignable(ctx, "intersection member", to, member).result()) {
                    return true;
                }
            }
            return false;
        }
        if (from instanceof UnionType) {
            for (DType member : ((UnionType) from).members()) {
                if (!new IsAssignable(ctx, "union member", to, member).result()) {
                    return false;
                }
            }
            return true;
        }
        return to._isAssignable(ctx, from);
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

    public void substitute(PlaceholderType placeholder, DType substituted) {
        tempSub.put(placeholder, substituted);
    }

    public static boolean $(DType to, DType from) {
        return new IsAssignable(to, from).result();
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
}
