package com.dexscript.transpiler;

class TempVariableContext {

    private int index = 0;

    public String assignVariableName() {
        index++;
        return lastVariableName();
    }

    public String lastVariableName() {
        return "tmp" + index + "__";
    }
}
