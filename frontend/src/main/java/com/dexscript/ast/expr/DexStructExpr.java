package com.dexscript.ast.expr;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexStructExpr extends DexExpr {

    private int structExprEnd = -1;
    private List<DexNamedArg> fields;
    private DexSyntaxError syntaxError;

    public DexStructExpr(Text src) {
        super(src);
        new Parser();
    }

    public static DexStructExpr $(String src) {
        DexStructExpr expr = new DexStructExpr(new Text(src));
        expr.attach(DexPackage.DUMMY);
        return expr;
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int end() {
        if (structExprEnd == -1) {
            throw new IllegalStateException();
        }
        return structExprEnd;
    }

    @Override
    public boolean matched() {
        return structExprEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (fields() != null) {
            for (DexNamedArg field : fields()) {
                if (field.name() != null) {
                    visitor.visit(field.name());
                }
                if (field.val() != null) {
                    visitor.visit(field.val());
                }
            }
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public List<DexNamedArg> fields() {
        return fields;
    }

    private class Parser {

        int i = src.begin;
        DexIdentifier name;

        Parser() {
            State.Play(this::leftBrace);
        }

        @Expect("{")
        State leftBrace() {
            for (;i < src.end;i++) {
                byte b= src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '{') {
                    i += 1;
                    return this::firstFieldName;
                }
                return null;
            }
            return null;
        }

        @Expect("identifier")
        State firstFieldName() {
            name = new DexIdentifier(src.slice(i));
            if (!name.matched()) {
                return null;
            }
            i = name.end();
            return this::firstColon;
        }

        @Expect(":")
        State firstColon() {
            for (;i < src.end;i++) {
                byte b= src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ':') {
                    fields = new ArrayList<>();
                    i += 1;
                    return this::fieldVal;
                }
                return null;
            }
            return null;
        }

        @Expect("expression")
        State fieldVal() {
            DexExpr val = DexExpr.parse(src.slice(i));
            if (!val.matched()) {
                return this::missingFieldVal;
            }
            fields.add(new DexNamedArg(name, val));
            i = val.end();
            return this::commaOrRightBrace;
        }

        @Expect(",")
        @Expect("}")
        private State commaOrRightBrace() {
            for (;i < src.end;i++) {
                byte b= src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::fieldName;
                }
                if (b == '}') {
                    structExprEnd = i + 1;
                    return null;
                }
                return this::missingRightBrace;
            }
            return this::missingRightBrace;
        }

        @Expect("identifier")
        State fieldName() {
            name = new DexIdentifier(src.slice(i));
            if (!name.matched()) {
                return this::missingFieldName;
            }
            i = name.end();
            return this::colon;
        }

        @Expect(":")
        State colon() {
            for (;i < src.end;i++) {
                byte b= src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ':') {
                    i += 1;
                    return this::fieldVal;
                }
                return this::missingColon;
            }
            return this::missingColon;
        }

        State missingColon() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    structExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::fieldVal;
                }
                if (b == ',') {
                    i += 1;
                    return this::fieldName;
                }
                if (b == '}') {
                    structExprEnd = i + 1;
                    return null;
                }
            }
            structExprEnd = i;
            return null;
        }

        State missingFieldName() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    structExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::colon;
                }
                if (b == ',') {
                    i += 1;
                    return this::fieldName;
                }
                if (b == ':') {
                    i += 1;
                    return this::fieldVal;
                }
                if (b == '}') {
                    structExprEnd = i + 1;
                    return null;
                }
            }
            structExprEnd = i;
            return null;
        }

        State missingRightBrace() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    structExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::fieldName;
                }
                if (b == ':') {
                    i += 1;
                    return this::fieldVal;
                }
            }
            structExprEnd = i;
            return null;
        }

        State missingFieldVal() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    structExprEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::commaOrRightBrace;
                }
                if (b == ',') {
                    i += 1;
                    return this::fieldName;
                }
                if (b == ':') {
                    i += 1;
                    return this::fieldVal;
                }
                if (b == '}') {
                    structExprEnd = i + 1;
                    return null;
                }
            }
            structExprEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
