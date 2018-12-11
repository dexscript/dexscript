package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.inf.DexInfFunction;
import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.ast.inf.DexInfTypeParam;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.LineEnd;

import java.util.ArrayList;
import java.util.List;

public class DexInterfaceBody extends DexElement {

    private final Text matched;
    private List<DexInfTypeParam> typeParams;
    private List<DexInfMethod> methods;
    private List<DexInfFunction> functions;
    private DexSyntaxError syntaxError;

    public DexInterfaceBody(Text src) {
        super(src);
        DexTopLevelDecl nextRootDecl = new DexTopLevelDecl(src);
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
        for (DexInfTypeParam typeParam : typeParams()) {
            visitor.visit(typeParam);
        }
        for (DexInfMethod method : methods()) {
            visitor.visit(method);
        }
        for (DexInfFunction function : functions()) {
            visitor.visit(function);
        }
    }

    public List<DexInfMethod> methods() {
        parse();
        return methods;
    }

    public List<DexInfFunction> functions() {
        parse();
        return functions;
    }

    public List<DexInfTypeParam> typeParams() {
        parse();
        return typeParams;
    }

    private void parse() {
        if (typeParams == null) {
            typeParams = new ArrayList<>();
            methods = new ArrayList<>();
            functions = new ArrayList<>();
            new Parser();
        }
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
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '{') {
                    i += 1;
                    return this::typeParameterOrMethodOrFunctionOrRightBrace;
                }
                return null;
            }
            return null;
        }

        @Expect("paramType parameter")
        @Expect("method")
        @Expect("function")
        @Expect("}")
        State typeParameterOrMethodOrFunctionOrRightBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '}') {
                    return null;
                }
                break;
            }
            DexInfTypeParam typeParam = new DexInfTypeParam(src.slice(i));
            if (typeParam.matched()) {
                typeParams.add(typeParam);
                i = typeParam.end();
                return this::typeParameterOrMethodOrFunctionOrRightBrace;
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
            return this::missingTypeParameter;
        }

        @Expect("method")
        @Expect("function")
        @Expect("}")
        State methodOrFunctionOrRightBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (b == '}') {
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

        State missingTypeParameter() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    i += 1;
                    return this::typeParameterOrMethodOrFunctionOrRightBrace;
                }
                if ('}' == b) {
                    return null;
                }
            }
            return null;
        }

        State missingMethodOrFunction() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    i += 1;
                    return this::methodOrFunctionOrRightBrace;
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
