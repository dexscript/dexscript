package com.dexscript.type;


import java.util.ArrayList;
import java.util.List;

public class IsAssignable {

    private final DType to;
    private final DType from;

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
    }

    private boolean result;
    private final List<Log> logs = new ArrayList<>();

    public IsAssignable(DType to, DType from) {
        this.to = to;
        this.from = from;
        result = to._isSubType(this, from);
    }

    public IsAssignable(IsAssignable parent, String comparing, DType to, DType from) {
        this.to = to;
        this.from = from;
        result = to._isSubType(this, from);
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
