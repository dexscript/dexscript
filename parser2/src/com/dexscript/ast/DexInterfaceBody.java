package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.inf.DexInfMember;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexInterfaceBody extends DexElement {

    private final Text matched;
    private List<DexInfMember> members;

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

    public void reparent(DexFunction parent) {
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

        private State leftBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == '{') {
                    i += 1;
                    return this::stmtOrRightBrace;
                }
                return null;
            }
            return null;
        }

        private State stmtOrRightBrace() {
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
                return this::stmtOrRightBrace;
            }
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
