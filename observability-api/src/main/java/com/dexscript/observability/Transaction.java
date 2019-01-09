package com.dexscript.observability;

import java.util.List;
import java.util.Map;

public class Transaction {
    public String ns;
    public Map<String, String> attributes;
    public List<String> argNames;
    public List<Object> argValues;
}
