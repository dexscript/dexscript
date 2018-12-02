package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.inf.DexInfMember;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexInterfaceBody extends DexElement {

    private final Text matched;
    private List<DexInfMember> members;
    private DexSyntaxError syntaxError;

    public DexInterfaceBody(Text src) {
        super(src);
        DexRootDecl nextRootDecl = new DexRootDecl(src);
        if (nextRootDecl.matched()) {
            matched = new Text(src.bytes, src.begin, nextRootDecl.begin());
        } else {
            matched = src;
        }
    }

    @Override
    public int begin() {
        return matched.begin;
    }

    public int end() {
        return matched.end;
    }

    @Override
    public boolean matched() {
        return true;
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public void reparent(DexInterface parent) {
        this.parent = parent;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (members() != null) {
            for (DexInfMember stmt : members()) {
                visitor.visit(stmt);
            }
        }
    }

    public List<DexInfMember> members() {
        if (members == null) {
            members = new ArrayList<>();
            new Parser();
        }
        return members;
    }

    private class Parser {

        int i = src.begin;
        DexInfMember thePrevOfChild = null;

        Parser() {
            State.Play(this::leftBrace);
        }

        @Expect("{")
        State leftBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '{') {
                    i += 1;
                    return this::memberOrRightBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("interface member")
        @Expect("}")
        State memberOrRightBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (LineEnd.__(b)) {
                    continue;
                }
                if (b == '}') {
                    return null;
                }
                break;
            }
            DexInfMember stmt = DexInfMember.parse(src.slice(i));
            stmt.reparent(DexInterfaceBody.this, thePrevOfChild);
            thePrevOfChild = stmt;
            members.add(stmt);
            if (stmt.matched()) {
                i = stmt.end();
                return this::memberOrRightBrace;
            }
            return this::unmatchedMember;
        }

        State unmatchedMember() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.__(b)) {
                    return this::memberOrRightBrace;
                }
                if ('}' == b) {
                    return null;
                }
            }
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
