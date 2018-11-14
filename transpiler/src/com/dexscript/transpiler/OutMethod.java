package com.dexscript.transpiler;

import com.dexscript.psi.OutCode;

public class OutMethod extends OutCode {

    private OutClass oClass;

    public OutMethod(OutClass oClass) {
        super(oClass);
        this.oClass = oClass;
        oClass.addMethod(this);
    }

    public OutClass oClass() {
        return oClass;
    }
}
