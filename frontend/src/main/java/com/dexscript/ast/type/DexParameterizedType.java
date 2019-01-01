package com.dexscript.ast.type;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexParameterizedType extends DexType {

    private static final int LEFT_RANK = 10;
    private final DexType genericType;
    private int expansionTypeEnd = -1;
    private List<DexType> typeArgs;
    private DexSyntaxError syntaxError;

    public DexParameterizedType(Text src, DexType genericType) {
        super(src);
        this.genericType = genericType;
        new Parser();
    }

    @Override
    public int leftRank() {
        return LEFT_RANK;
    }

    @Override
    public int begin() {
        return genericType.begin();
    }

    @Override
    public int end() {
        if (expansionTypeEnd == -1) {
            throw new IllegalStateException();
        }
        return expansionTypeEnd;
    }

    @Override
    public boolean matched() {
        return expansionTypeEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (typeArgs() != null) {
            for (DexType typeArg : typeArgs()) {
                visitor.visit(typeArg);
            }
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public List<DexType> typeArgs() {
        return typeArgs;
    }

    public DexType genericType() {
        return genericType;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::leftAngleBracket);
        }

        @Expect("<")
        State leftAngleBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                // prevent match <> on the next line
                if (LineEnd.$(b)) {
                    return null;
                }
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '<') {
                    i += 1;
                    genericType.reparent(DexParameterizedType.this);
                    return this::firstTypeArg;
                }
                return null;
            }
            return null;
        }

        @Expect("type")
        State firstTypeArg() {
            typeArgs = new ArrayList<>();
            DexType typeArg = DexType.parse(src.slice(i), 0);
            typeArg.reparent(DexParameterizedType.this);
            typeArgs.add(typeArg);
            if (!typeArg.matched()) {
                return this::missingTypeArg;
            }
            i = typeArg.end();
            return this::commaOrRightAngleBracket;
        }

        @Expect(",")
        @Expect(">")
        State commaOrRightAngleBracket() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::moreTypeArgs;
                }
                if (b == '>') {
                    expansionTypeEnd = i + 1;
                    return null;
                }
                return this::missingComma;
            }
            return this::missingComma;
        }

        @Expect("type")
        State moreTypeArgs() {
            DexType typeArg = DexType.parse(src.slice(i), 0);
            typeArg.reparent(DexParameterizedType.this);
            typeArgs.add(typeArg);
            if (!typeArg.matched()) {
                return this::missingTypeArg;
            }
            i = typeArg.end();
            return this::commaOrRightAngleBracket;
        }

        State missingComma() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    expansionTypeEnd = i;
                    return null;
                }
                if (b == '>') {
                    expansionTypeEnd = i + 1;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::moreTypeArgs;
                }
            }
            expansionTypeEnd = i;
            return null;
        }

        State missingTypeArg() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    expansionTypeEnd = i;
                    return null;
                }
                if (b == '>') {
                    expansionTypeEnd = i + 1;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::commaOrRightAngleBracket;
                }
                if (b == ',') {
                    i += 1;
                    return this::moreTypeArgs;
                }
            }
            expansionTypeEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
