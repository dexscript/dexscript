package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.inf.DexInterfaceStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexInterfaceBody extends DexElement {

    private final Text matched;
    private List<DexInterfaceStatement> stmts;

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
        if (stmts() != null) {
            for (DexInterfaceStatement stmt : stmts()) {
                visitor.visit(stmt);
            }
        }
    }

    public List<DexInterfaceStatement> stmts() {
        if (stmts == null) {
            stmts = new ArrayList<>();
            new Parser();
        }
        return stmts;
    }

    private class Parser {

        int i = src.begin;
        DexInterfaceStatement thePrevOfChild = null;

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
            DexInterfaceStatement stmt = DexInterfaceStatement.parse(src.slice(i));
            stmt.reparent(DexInterfaceBody.this, thePrevOfChild);
            thePrevOfChild = stmt;
            stmts.add(stmt);
            if (stmt.matched()) {
                i = stmt.end();
                return this::stmtOrRightBrace;
            }
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
