package com.dexscript.transpile.type;

import com.dexscript.type.NamedType;
import com.dexscript.type.NamedTypesProvider;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorTable implements NamedTypesProvider {

    private final Map<String, List<ActorType>> defined = new HashMap<>();

    public ActorTable(TypeSystem ts) {
        ts.lazyDefineTypes(this);
    }

    public void define(ActorType actor) {
        List<ActorType> actors = defined.computeIfAbsent(actor.name(), k -> new ArrayList<>());
        actors.add(actor);
    }

    @Override
    public List<NamedType> namedTypes() {
        List<NamedType> types = new ArrayList<NamedType>();
        for (Map.Entry<String, List<ActorType>> entry : defined.entrySet()) {
            if (entry.getValue().size() == 1) {
                types.add(entry.getValue().get(0));
            } else {
                types.add(new ActorUnionType(entry.getKey(), entry.getValue()));
            }
        }
        return types;
    }
}
