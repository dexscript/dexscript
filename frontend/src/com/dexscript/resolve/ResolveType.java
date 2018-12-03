package com.dexscript.resolve;

final class ResolveType {

//    private final Map<String, List<DexFunction>> definedFunctions = new HashMap<>();
//    private final DenotationTable<Type> defined = BuiltinTypes.BUILTIN_TYPES;
//    private final ResolveType resolve;
//
//    ResolveType(ResolveType resolve) {
//        this.resolve = resolve;
//    }
//
//    public void define(Denotation.InterfaceType inf) {
//        defined.put(inf.name(), inf);
//    }
//
//    public Type resolveType(DexReference ref) {
//        Type type = ref.attachmentOfType(Type.class);
//        if (type != null) {
//            return type;
//        }
//        String refName = ref.toString();
//        type = defined.get(refName);
//        if (type != null) {
//            return type;
//        }
//        List<DexFunction> functions = definedFunctions.get(refName);
//        if (functions != null) {
//            for (DexFunction function : functions) {
//                return resolve.resolveType(function);
//            }
//        }
//        type = BuiltinTypes.UNDEFINED_TYPE;
//        ref.attach(type);
//        return type;
//    }
//
//    public void define(DexFunction function) {
//        String functionName = function.identifier().toString();
//        List<DexFunction> functions = definedFunctions.computeIfAbsent(functionName, k -> new ArrayList<>());
//        functions.add(function);
//    }
}
