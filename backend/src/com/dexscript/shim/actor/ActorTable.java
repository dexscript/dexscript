package com.dexscript.shim.actor;

import com.dexscript.ast.core.DexSyntaxException;
import com.dexscript.type.NamedType;
import com.dexscript.type.NamedTypesProvider;
import com.dexscript.type.TypeSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorTable implements NamedTypesProvider {

    private final Map<String, List<ActorType>> defined = new HashMap<>();
    private final TypeSystem ts;

    public ActorTable(TypeSystem ts) {
        this.ts = ts;
        ts.lazyDefineTypes(this);
    }

    public void define(ActorType actor) {
        List<ActorType> actors = defined.computeIfAbsent(actor.name(), k -> new ArrayList<>());
        if (!actors.isEmpty()) {
            String expectedFileName = actors.get(0).elem().file().fileName();
            String actualFileName = actor.elem().file().fileName();
            if (!actualFileName.equals(expectedFileName)) {
                throw new DexSyntaxException("actor of same name should be defined in same file: " + actor.name());
            }
        }
        actors.add(actor);
    }

    @Override
    public List<NamedType> namedTypes() {
        List<NamedType> types = new ArrayList<NamedType>();
        for (Map.Entry<String, List<ActorType>> entry : defined.entrySet()) {
            if (entry.getValue().size() == 1) {
                types.add(entry.getValue().get(0));
            } else {
                types.add(new ActorUnionType(ts, entry.getKey(), entry.getValue()));
            }
        }
        return types;
    }

    public List<ActorType> actors() {
        List<ActorType> allActors = new ArrayList<>();
        for (List<ActorType> actors : defined.values()) {
            allActors.addAll(actors);
        }
        return allActors;
    }
}
