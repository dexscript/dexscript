package com.dexscript.infer;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.expr.DexExpr;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Keyword;
import com.dexscript.ast.type.DexType;
import com.dexscript.type.core.DType;
import com.dexscript.type.core.ResolveType;
import com.dexscript.type.core.TypeSystem;

import java.util.ArrayList;
import java.util.List;

public interface ResolvePosArgs {

    static List<DType> $(TypeSystem ts, String src) {
        return ResolvePosArgs.$(ts, new Text(src));
    }

    static List<DType> $(TypeSystem ts, Text src) {
        List<DType> args = new ArrayList<>();
        for (int i = 0; i < src.end; i++) {
            byte b = src.bytes[i];
            if (Blank.$(b) || b == ',') {
                continue;
            }
            if (Keyword.$(src, i, '(', 'c', 'o', 'n', 's', 't', ')')) {
                DexExpr arg = DexExpr.parse(src.slice(i + 7));
                arg.attach(DexPackage.DUMMY);
                if (!arg.matched()) {
                    throw new RuntimeException("unable to parse invocation args: " + src);
                }
                args.add(InferType.$(ts, arg));
                i = arg.end();
                continue;
            }
            DexType arg = DexType.parse(src.slice(i));
            arg.attach(DexPackage.DUMMY);
            if (!arg.matched()) {
                throw new RuntimeException("unable to parse invocation args: " + src);
            }
            args.add(ResolveType.$(ts, null, arg));
            i = arg.end();
        }
        return args;
    }
}
