package com.dexscript.observability;

import java.util.Map;

public final class Transaction {
    public String ns;
    public Map<String, String> attributes;
    public String[] argNames;
    public Object[] argValues;
    public long statValue;
}
