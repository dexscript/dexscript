package com.dexscript.ast.type;

import com.dexscript.ast.core.DexSyntaxError;
import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexInterfaceType extends DexType {

    private DexSyntaxError syntaxError;
    private List<DexInfMethod> methods;
    private List<DexInfFunction> functions;
    private int interfaceTypeEnd = -1;

    public DexInterfaceType(Text src) {
        super(src);
        new Parser();
    }

    public DexInterfaceType(String src) {
        this(new Text(src));
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int end() {
        if (interfaceTypeEnd == -1) {
            throw new IllegalStateException();
        }
        return interfaceTypeEnd;
    }

    @Override
    public boolean matched() {
        return interfaceTypeEnd != -1;
    }

    @Override
    public void walkDown(Visitor visitor) {
        if (methods() != null) {
            for (DexInfMethod method : methods()) {
                visitor.visit(method);
            }
        }
        if (functions() != null) {
            for (DexInfFunction function : functions()) {
                visitor.visit(function);
            }
        }
    }

    @Override
    public DexSyntaxError syntaxError() {
        return syntaxError;
    }

    public List<DexInfMethod> methods() {
        return methods;
    }

    public List<DexInfFunction> functions() {
        return functions;
    }

    private class Parser {

        int i = src.begin;

        Parser() {
            State.Play(this::interfaceKeyword);
        }

        @Expect("interface")
        State interfaceKeyword() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (Keyword.$(src, i, 'i', 'n', 't', 'e', 'r', 'f', 'a', 'c', 'e')) {
                    i += 9;
                    return this::leftBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("{")
        State leftBrace() {
            for (; i < src.end;i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '{') {
                    methods = new ArrayList<>();
                    functions = new ArrayList<>();
                    i += 1;
                    return this::methodOrFunctionOrRightBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("method")
        @Expect("function")
        @Expect("}")
        State methodOrFunctionOrRightBrace() {
            for (; i < src.end;i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '}') {
                    interfaceTypeEnd = i + 1;
                    return null;
                }
                break;
            }
            DexInfMethod method = new DexInfMethod(src.slice(i));
            if (method.matched()) {
                methods.add(method);
                i = method.end();
                return this::methodOrFunctionOrRightBrace;
            }
            DexInfFunction func = new DexInfFunction(src.slice(i));
            if (func.matched()) {
                functions.add(func);
                i = func.end();
                return this::methodOrFunctionOrRightBrace;
            }
            return this::missingMethodOrFunction;
        }

        State missingMethodOrFunction() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    interfaceTypeEnd = i;
                    return null;
                }
                if (Blank.$(b)) {
                    i += 1;
                    return this::methodOrFunctionOrRightBrace;
                }
            }
            interfaceTypeEnd = i;
            return null;
        }

        void reportError() {
            if (syntaxError == null) {
                syntaxError = new DexSyntaxError(src, i);
            }
        }
    }
}
