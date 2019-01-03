package com.dexscript.ast;

import com.dexscript.ast.core.*;
import com.dexscript.ast.inf.DexInfField;
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
    private List<DexInfField> fields;
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

    public List<DexInfField> fields() {
        parse();
        return fields;
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
            fields = new ArrayList<>();
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
                    return this::maybeTypeParameter;
                }
                return null;
            }
            return null;
        }

        @Expect("[type parameter]")
        State maybeTypeParameter() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b) || b == ';') {
                    continue;
                }
                if (b == '}') {
                    return null;
                }
                if (b == '<') {
                    DexInfTypeParam typeParam = new DexInfTypeParam(src.slice(i));
                    typeParam.reparent(DexInterfaceBody.this);
                    typeParams.add(typeParam);
                    i = typeParam.end();
                    return this::maybeTypeParameter;
                }
                break;
            }
            return this::methodOrFunctionOrFieldOrRightBrace;
        }

        @Expect("method")
        @Expect("function")
        @Expect("}")
        State methodOrFunctionOrFieldOrRightBrace() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b) || b == ';') {
                    continue;
                }
                if (b == '}') {
                    return null;
                }
                break;
            }
            DexInfFunction func = new DexInfFunction(src.slice(i));
            func.reparent(DexInterfaceBody.this);
            if (func.matched()) {
                functions.add(func);
                i = func.end();
                return this::methodOrFunctionOrFieldOrRightBrace;
            }
            DexInfMethod method = new DexInfMethod(src.slice(i));
            method.reparent(DexInterfaceBody.this);
            if (method.matched()) {
                methods.add(method);
                i = method.end();
                return this::methodOrFunctionOrFieldOrRightBrace;
            }
            DexInfField field = new DexInfField(src.slice(i));
            field.reparent(DexInterfaceBody.this);
            if (field.matched()) {
                fields.add(field);
                i = field.end();
                return this::methodOrFunctionOrFieldOrRightBrace;
            }
            return this::missingMember;
        }

        State missingMember() {
            reportError();
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (LineEnd.$(b)) {
                    i += 1;
                    return this::methodOrFunctionOrFieldOrRightBrace;
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
