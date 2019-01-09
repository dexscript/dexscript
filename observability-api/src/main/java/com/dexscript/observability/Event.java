package com.dexscript.observability;

import java.util.Map;

public final class Event {
    public String ns;
    public Map<String, String> attributes;
    public String[] argNames;
    public String[] argValues;
}
