package com.dexscript.transpiler;

import com.dexscript.psi.DexServeStatement;
import com.dexscript.psi.DexSignature;
import com.dexscript.psi.impl.DexPsiImplUtil;

public class OutInnerClass extends OutClass {

    private final OutClass oParent;

    public OutInnerClass(DexServeStatement iServeStmt, OutMethod oMethod) {
        super(oMethod.oClass(), iServeStmt.getIdentifier().getNode().getText() + "__");
        oParent = oMethod.oClass();
        appendSourceLine(iServeStmt);
        append("public class ");
        append(className());
        append(" extends Actor implements Result");
        DexSignature iSig = iServeStmt.getSignature();
        append(iSig.getResult() == null ? 0 : 1);
        append(" {");
        indent(() -> {
            appendNewLine();
            // oFields for return value
            appendReturnValueFields(iSig);
            appendNewLine();
            // constructor
            OutMethod oCtor = new OutMethod(this, iSig);
            oCtor.append("public ");
            oCtor.append(className());
            oCtor.append('(');
            int paramsCount = DexPsiImplUtil.getParamsCount(iSig);
            oCtor.appendParamsDeclaration(paramsCount);
            oCtor.append(") {");
            oCtor.indent(() -> {
                iServeStmt.getBlock().acceptChildren(oCtor);
            });
            oCtor.appendNewLine('}');
            genClassBody();
        });
        append('}');
        appendNewLine();
    }
}
