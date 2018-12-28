package com.dexscript.shim.actor;

import com.dexscript.ast.DexPackage;
import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.type.NamedType;
import com.dexscript.type.NamedTypesProvider;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorTable {

    private final TypeSystem ts;
    private final Map<DexPackage, PackageActors> defined = new HashMap<>();

    public ActorTable(TypeSystem ts) {
        this.ts = ts;
    }

    public void define(ActorType actor) {
        DexPackage pkg = actor.elem().pkg();
        PackageActors pkgActors = defined.computeIfAbsent(pkg, k -> new PackageActors(pkg, ts));
        List<ActorType> sameNameActors = pkgActors.computeIfAbsent(actor.name(), k -> new ArrayList<>());
        if (!sameNameActors.isEmpty()) {
            String expectedFileName = sameNameActors.get(0).elem().file().fileName();
            String actualFileName = actor.elem().file().fileName();
            if (!actualFileName.equals(expectedFileName)) {
                throw new DexSyntaxException("actor of same name should be defined in same file: " + actor.name());
            }
        }
        sameNameActors.add(actor);
    }

    public List<ActorType> actors() {
        List<ActorType> allActors = new ArrayList<>();
        for (PackageActors pkgActors : defined.values()) {
            for (List<ActorType> actors : pkgActors.values()) {
                allActors.addAll(actors);
            }
        }
        return allActors;
    }

    private static class PackageActors extends HashMap<String, List<ActorType>> implements NamedTypesProvider {

        private final TypeSystem ts;

        private PackageActors(DexPackage pkg, TypeSystem ts) {
            this.ts = ts;
            ts.lazyDefineTypes(pkg, this);
        }

        @Override
        public List<NamedType> namedTypes() {
            List<NamedType> types = new ArrayList<NamedType>();
            for (Map.Entry<String, List<ActorType>> entry : entrySet()) {
                if (entry.getValue().size() == 1) {
                    types.add(entry.getValue().get(0));
                } else {
                    types.add(new ActorUnionType(ts, entry.getKey(), entry.getValue()));
                }
            }
            return types;
        }
    }
}
