package com.dexscript.ast.inf;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.type.DexType;

public class DexInfField extends DexElement {

    private final DexParam dexParam;

    public DexInfField(Text src) {
        super(src);
        dexParam = new DexParam(src);
        dexParam.paramName().reparent(this);
        dexParam.paramType().reparent(this);
    }

    public static DexInfField $(String src) {
        return new DexInfField(new Text(src));
    }

    @Override
    public int end() {
        return dexParam.end();
    }

    @Override
    public boolean matched() {
        return dexParam.matched();
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (fieldName() != null) {
            visitor.visit(fieldName());
        }
        if (fieldType() != null) {
            visitor.visit(fieldType());
        }
    }

    public DexIdentifier fieldName() {
        return dexParam.paramName();
    }

    public DexType fieldType() {
        return dexParam.paramType();
    }
}
