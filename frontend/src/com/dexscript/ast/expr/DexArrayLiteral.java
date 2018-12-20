package com.dexscript.ast.expr;

import com.dexscript.ast.core.*;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexArrayLiteral extends DexExpr {

    private int arrayLiteralEnd = -1;
    private List<DexExpr> elems;
    private DexSyntaxError syntaxError;

    public DexArrayLiteral(Text src) {
        super(src);
        new Parser();
    }

    public DexArrayLiteral(String src) {
        this(new Text(src));
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int end() {
        if (arrayLiteralEnd == -1) {
            throw new IllegalStateException();
        }
        return arrayLiteralEnd;
    }

    @Override
    public boolean matched() {
        return arrayLiteralEnd != -1;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (elems() != null) {
            for (DexExpr elem : elems()) {
                visitor.visit(elem);
            }
        }
    }

    @Override
    public void reparent(DexElement parent, DexStatement stmt) {
        this.parent = parent;
        this.stmt = stmt;
        if (elems() != null) {
            for (DexExpr elem : elems()) {
                elem.reparent(this, stmt);
            }
        }
    }

    public List<DexExpr> elems() {
        return elems;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::leftBracket);
        }

        @Expect("[")
        State leftBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '[') {
                    i += 1;
                    elems = new ArrayList<>();
                    return this::elemOrRightBracket;
                }
                return null;
            }
            return null;
        }

        @Expect("expression")
        @Expect("]")
        State elemOrRightBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ']') {
                    arrayLiteralEnd = i + 1;
                    return null;
                }
                break;
            }
            DexExpr elem = DexExpr.parse(src.slice(i));
            if (!elem.matched()) {
                return this::missingElem;
            }
            elems.add(elem);
            i = elem.end();
            return this::commaOrRightBracket;
        }

        @Expect(",")
        @Expect("]")
        State commaOrRightBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::elemOrRightBracket;
                }
                if (b == ']') {
                    arrayLiteralEnd = i + 1;
                    return null;
                }
                return this::missingRightBracket;
            }
            return this::missingRightBracket;
        }

        State missingRightBracket() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    arrayLiteralEnd = i;
                    return null;
                }
            }
            arrayLiteralEnd = i;
            return null;
        }

        State missingElem() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    arrayLiteralEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::commaOrRightBracket;
                }
                if (b == ',') {
                    i += 1;
                    return this::elemOrRightBracket;
                }
            }
            arrayLiteralEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
