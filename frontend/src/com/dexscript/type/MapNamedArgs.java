package com.dexscript.type;

import java.util.ArrayList;
import java.util.List;

class MapNamedArgs {

    public List<DType> args = new ArrayList<>();
    public int[] mapping;

    public static MapNamedArgs $(FunctionType func, List<DType> posArgs, List<NamedArg> namedArgs) {
        if (func.params().size() != posArgs.size() + namedArgs.size()) {
            // not enough argument
            return null;
        }
        MapNamedArgs mapNamedArgs = new MapNamedArgs();
        mapNamedArgs.mapping = new int[namedArgs.size()];
        mapNamedArgs.args.addAll(posArgs);
        for (int i = posArgs.size(); i < func.params().size(); i++) {
            FunctionParam param = func.params().get(i);
            DType argType = null;
            for (int j = 0; j < namedArgs.size(); j++) {
                NamedArg namedArg = namedArgs.get(j);
                if (namedArg.name().equals(param.name())) {
                    mapNamedArgs.mapping[j] = i;
                    argType = namedArg.type();
                    break;
                }
            }
            if (argType == null) {
                // missing param n
                return null;
            }
            mapNamedArgs.args.add(argType);
        }
        return mapNamedArgs;
    }
}
