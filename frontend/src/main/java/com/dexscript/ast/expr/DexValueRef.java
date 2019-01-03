package com.dexscript.ast.expr;

import com.dexscript.ast.core.*;
import com.dexscript.ast.token.A2Z;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.Zero2Nine;

import java.util.Collections;

public class DexValueRef extends DexLeafExpr implements DexInvocationExpr {

    private boolean isGlobalScope;
    private Text matched;
    private DexInvocation invocation;

    public DexValueRef(Text src) {
        super(src);
        new Parser();
    }

    public static DexValueRef $(String src) {
        return new DexValueRef(new Text(src));
    }

    public boolean matched() {
        return matched != null;
    }

    public int begin() {
        if (matched == null) {
            throw new IllegalStateException();
        }
        return matched.begin;
    }

    public int end() {
        if (matched == null) {
            throw new IllegalStateException();
        }
        return matched.end;
    }

    @Override
    public int leftRank() {
        return 0;
    }

    public boolean isGlobalScope() {
        return isGlobalScope;
    }

    @Override
    public DexInvocation invocation() {
        if (!isGlobalScope) {
            throw new DexSyntaxException("only global scope value reference is invocation");
        }
        if (invocation == null) {
            String fieldName = toString();
            String funcName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            invocation = new DexInvocation(pkg(), funcName, Collections.emptyList());
        }
        return invocation;
    }

    private class Parser {

        int i;
        int valueRefBegin;

        Parser() {
            i = src.begin;
            State.Play(this::scope);
        }

        @Expect("::")
        State scope() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, ':', ':')) {
                    isGlobalScope = true;
                    i += 2;
                    return this::firstChar;
                }
                return this::firstChar;
            }
            return null;
        }

        @Expect("blank")
        @Expect("$")
        @Expect("a~z")
        @Expect("A~Z")
        State firstChar() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '$') {
                    valueRefBegin = i;
                    i += 1;
                    return this::matchedDollar;
                }
                if (A2Z.$(b)) {
                    valueRefBegin = i;
                    i += 1;
                    return this::remainingChars;
                }
                return null;
            }
            return null;
        }

        @Expect("a~z")
        @Expect("A~Z")
        @Expect("0~9")
        @Expect("_")
        State remainingChars() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (A2Z.$(b) || Zero2Nine.$(b) || b == '_') {
                    continue;
                }
                break;
            }
            matched = new Text(src.bytes, valueRefBegin, i);
            return null;
        }

        State matchedDollar() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (A2Z.$(b) || Zero2Nine.$(b) || b == '_') {
                    return null;
                }
                break;
            }
            matched = new Text(src.bytes, valueRefBegin, i);
            return null;
        }
    }
}
