package com.dexscript.ast.stmt;

import com.dexscript.ast.core.*;
import com.dexscript.ast.token.Blank;

import java.util.ArrayList;
import java.util.List;

public class DexBlock implements DexStatement {

    private final Text src;
    private int blockBegin = -1;
    private int blockEnd = -1;
    private List<DexStatement> stmts;

    // for walk up
    private DexElement parent;
    private DexStatement prev;

    public DexBlock(String src) {
        this(new Text(src));
    }

    public DexBlock(Text src) {
        this.src = src;
        new Parser();
    }

    @Override
    public void reparent(DexElement parent, DexStatement prev) {
        this.parent = parent;
        this.prev = prev;
    }

    @Override
    public DexStatement prev() {
        return prev;
    }

    @Override
    public DexElement parent() {
        return parent;
    }

    @Override
    public Text src() {
        return src;
    }

    @Override
    public int begin() {
        if (blockBegin == -1) {
            throw new IllegalStateException();
        }
        return blockBegin;
    }

    @Override
    public int end() {
        if (blockEnd == -1) {
            throw new IllegalStateException();
        }
        return blockEnd;
    }

    @Override
    public boolean matched() {
        return blockEnd != -1;
    }

    @Override
    public DexError err() {
        return null;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (stmts() != null) {
            for (DexStatement stmt : stmts()) {
                visitor.visit(stmt);
            }
        }
    }

    @Override
    public String toString() {
        return DexElement.describe(this);
    }

    public List<DexStatement> stmts() {
        return stmts;
    }

    private class Parser {

        int i = src.begin;
        DexStatement thePrevOfChild = null;

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
                    stmts = new ArrayList<>();
                    blockBegin = i;
                    i += 1;
                    return this::statement;
                }
            }
            return null;
        }

        @Expect("statement")
        State statement() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (b == ';') {
                    continue;
                }
                if (b == '}') {
                    blockEnd = i + 1;
                    return null;
                }
                break;
            }
            DexStatement stmt = DexStatement.parse(new Text(src.bytes, i, src.end));
            stmt.reparent(DexBlock.this, thePrevOfChild);
            thePrevOfChild = stmt;
            stmts.add(stmt);
            if (stmt.matched()) {
                i = stmt.end();
                return this::statement;
            }
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
