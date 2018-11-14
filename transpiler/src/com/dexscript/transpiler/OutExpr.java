package com.dexscript.transpiler;

import com.dexscript.psi.DexCallExpr;
import com.dexscript.psi.DexStringLiteral;
import org.jetbrains.annotations.NotNull;

public class OutExpr extends OutValue {

    private OutMethod oMethod;
    private OutClass oClass;

    OutExpr(OutMethod oMethod) {
        super(oMethod.iFile());
        this.oMethod = oMethod;
        oClass = oMethod.oClass();
    }

    @Override
    public void visitStringLiteral(@NotNull DexStringLiteral o) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "String");
        String text = o.getNode().getText();
        append('"');
        append(text.substring(1, text.length() - 1));
        append('"');
    }

    @Override
    public void visitCallExpr(@NotNull DexCallExpr o) {
        type = new RuntimeType(RuntimeTypeKind.CONCRETE_OBJECT, "Result");
        String funcName = o.getExpression().getNode().getText();
        oClass.referenced(o);
        String fieldName = oMethod.oClass().addField(funcName, "Result");
        oMethod.append(fieldName);
        oMethod.append(" = ");
        oMethod.append(oMethod.oClass().shimClassName());
        oMethod.append('.');
        oMethod.append(funcName);
        oMethod.append("();");
        oMethod.appendNewLine();
        append(fieldName);
    }
}
