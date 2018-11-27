package com.dexscript.ast.func;

import com.dexscript.ast.core.*;
import com.dexscript.ast.token.Blank;

import java.util.ArrayList;
import java.util.List;

public class DexBlock extends DexStatement {

    private int blockBegin = -1;
    private int blockEnd = -1;
    private List<DexStatement> stmts;

    public DexBlock(String src) {
        this(new Text(src));
    }

    public DexBlock(Text src) {
        super(src);
        new Parser();
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
    public void walkDown(Visitor visitor) {
        if (stmts() != null) {
            for (DexStatement stmt : stmts()) {
                visitor.visit(stmt);
            }
        }
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
                    return this::stmtOrRightBrace;
                }
            }
            return null;
        }

        @Expect("stmtOrRightBrace")
        State stmtOrRightBrace() {
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
            DexStatement stmt = DexStatement.parse(src.slice(i));
            stmt.reparent(DexBlock.this, thePrevOfChild);
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