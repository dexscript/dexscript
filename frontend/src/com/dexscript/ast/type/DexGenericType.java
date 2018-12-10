package com.dexscript.ast.type;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;

import java.util.ArrayList;
import java.util.List;

public class DexGenericType extends DexType {

    private static final int LEFT_RANK = 10;
    private static final int RIGHT_RANK = 10;
    private final DexType genericType;
    private int genericTypeEnd = -1;
    private List<DexType> typeArgs;

    public DexGenericType(Text src, DexType genericType) {
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
        if (genericTypeEnd == -1) {
            throw new IllegalStateException();
        }
        return genericTypeEnd;
    }

    @Override
    public boolean matched() {
        return genericTypeEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (typeArgs() != null) {
            for (DexType typeArg : typeArgs()) {
                visitor.visit(typeArg);
            }
        }
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
            for (;i<src.end;i++) {
                byte b= src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '<') {
                    i += 1;
                    return this::firstTypeArg;
                }
                return null;
            }
            return null;
        }

        @Expect("type")
        State firstTypeArg() {
            typeArgs = new ArrayList<>();
            DexType typeArg = DexType.parse(src.slice(i), RIGHT_RANK);
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
            for (;i<src.end;i++) {
                byte b= src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == ',') {
                    i += 1;
                    return this::moreTypeArgs;
                }
                if (b == '>') {
                    genericTypeEnd = i + 1;
                    return null;
                }
                return this::missingComma;
            }
            return this::missingComma;
        }

        @Expect("type")
        State moreTypeArgs() {
            DexType typeArg = DexType.parse(src.slice(i), RIGHT_RANK);
            if (!typeArg.matched()) {
                return this::missingTypeArg;
            }
            return this::commaOrRightAngleBracket;
        }

        State missingComma() {
            throw new UnsupportedOperationException("not implemented");
        }

        State missingTypeArg() {
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
