package com.dexscript.infer;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexIdentifier;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.type.DexType;
import com.dexscript.type.core.*;

import java.util.ArrayList;
import java.util.List;

public interface ResolveNamedArgs {

    static List<NamedArg> $(TypeSystem ts, String src) {
        return ResolveNamedArgs.$(ts, new Text(src));
    }

    static List<NamedArg> $(TypeSystem ts, Text src) {
        List<NamedArg> args = new ArrayList<>();
        for (int i = src.begin; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b) || b == ',') {
                continue;
            }
            DexIdentifier name = new DexIdentifier(src.slice(i));
            if (!name.matched()) {
                throw new RuntimeException("unable to resolve named args: " + src);
            }
            if (src.bytes[name.end()] != '=') {
                throw new RuntimeException("expect equal sign: " + src);
            }
            i = name.end() + 1;
            if (Keyword.$(src, i, '(', 'c', 'o', 'n', 's', 't', ')')) {
                DexExpr arg = DexExpr.parse(src.slice(i + 7));
                arg.attach(DexPackage.DUMMY);
                if (!arg.matched()) {
                    throw new RuntimeException("unable to parse invocation args: " + src);
                }
                args.add(new NamedArg(name.toString(), InferType.$(ts, arg)));
                i = arg.end();
                continue;
            }
            DexType arg = DexType.parse(src.slice(i));
            arg.attach(DexPackage.DUMMY);
            if (!arg.matched()) {
                throw new RuntimeException("unable to parse invocation args: " + src);
            }
            args.add(new NamedArg(name.toString(), InferType.$(ts, null, arg)));
            i = arg.end();
        }
        return args;
    }
}
