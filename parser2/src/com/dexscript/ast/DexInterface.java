package com.dexscript.ast;

import com.dexscript.ast.core.DexElement;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.stmt.DexStatement;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;

import java.util.List;

public class DexInterface extends DexElement {

    private int interfaceBegin = -1;
    private int bodyBegin = -1;
    private DexIdentifier identifier;
    private DexInterfaceBody body;

    public DexInterface(Text src) {
        super(src);
        new Parser();
    }

    public DexInterface(String src) {
        this(new Text(src));
    }

    @Override
    public int begin() {
        return interfaceBegin;
    }

    @Override
    public int end() {
        return body().end();
    }

    @Override
    public boolean matched() {
        return bodyBegin != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        visitor.visit(body);
    }

    public DexIdentifier identifier() {
        return identifier;
    }

    public DexInterfaceBody body() {
        if (bodyBegin == -1) {
            throw new IllegalStateException();
        }
        if (body == null) {
            body = new DexInterfaceBody(src.slice(bodyBegin));
        }
        return body;
    }

    public List<DexStatement> stmts() {
        return body().stmts();
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::interfaceKeyword);
        }

        @Expect("interface")
        private State interfaceKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.__(b)) {
                    continue;
                }
                if (Keyword.__(src, i,
                        'i', 'n', 't', 'e', 'r', 'f', 'a', 'c', 'e')) {
                    interfaceBegin = i;
                    i = i + 9;
                    return this::blank;
                }
                return null;
            }
            return null;
        }

        @Expect("blank")
        public State blank() {
            int j = 0;
            for (; i < src.end; i++, j++) {
                if (Blank.__(src.bytes[i])) {
                    continue;
                }
                break;
            }
            if (j > 0) {
                return this::identifier;
            }
            return null;
        }

        @Expect("identifier")
        private State identifier() {
            identifier = new DexIdentifier(src.slice(i));
            if (identifier.matched()) {
                i = identifier.end();
                return this::leftBrace;
            }
            return null;
        }

        @Expect("{")
        private State leftBrace() {
            for (; i < src.end; i++) {
                if (!Blank.__(src.bytes[i])) {
                    break;
                }
            }
            if (src.bytes[i] != '{') {
                return null;
            }
            // matched
            bodyBegin = i;
            return null;
        }
    }
}
