package com.dexscript.type;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.type.DexType;

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
