package com.dexscript.parser2.stmt;

import com.dexscript.parser2.core.*;
import com.dexscript.parser2.stmt.DexStatement;
import com.dexscript.parser2.token.Blank;

import java.util.ArrayList;
import java.util.List;

public class DexBlock implements DexElement {

    private final Text src;
    private DexError err;
    private int blockBegin = -1;
    private int blockEnd = -1;
    private List<DexStatement> stmts;

    public DexBlock(String src) {
        this(new Text(src));
    }

    public DexBlock(Text src) {
        this.src = src;
        new Parser();
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
        return err;
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
            DexStatement stmt = new DexStatement(new Text(src.bytes, i, src.end));
            stmts.add(stmt);
            if (stmt.matched()) {
                i = stmt.end();
                return this::statement;
            }
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
