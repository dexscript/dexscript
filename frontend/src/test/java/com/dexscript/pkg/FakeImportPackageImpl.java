package com.dexscript.pkg;

import com.dexscript.ast.DexActor;
import com.dexscript.ast.DexPackage;
import com.dexscript.ast.stmt.DexAwaitConsumer;
import com.dexscript.type.composite.ActorType;
import com.dexscript.type.core.FunctionType;
import com.dexscript.type.core.TypeSystem;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FakeImportPackageImpl implements ImportPackage.Impl {

    private final TypeSystem ts = new TypeSystem();
    private final Map<String, DexPackage> packageMap = new HashMap<>();

    @Override
    public DexPackage pkg(Path pkgPath) {
        String pathStr = pkgPath.toString().replace("/", ".");
        if (!packageMap.containsKey(pathStr)) {
            packageMap.put(pathStr, new DexPackage(pathStr));
        }
        return packageMap.get(pathStr);
    }

    @Override
    public TypeSystem typeSystem() {
        return ts;
    }

    @Override
    public void addActorType(ActorType actorType) {

    }

    @Override
    public Object callActor(FunctionType expandedFunc, DexActor actor) {
        return new Object();
    }

    @Override
    public Object newActor(FunctionType expandedFunc, DexActor actor) {
        return new Object();
    }

    @Override
    public Object newInnerActor(FunctionType expandedFunc, DexActor actor, DexAwaitConsumer awaitConsumer) {
        return new Object();
    }

    @Override
    public Object callInnerActor(FunctionType expandedFunc, DexActor actor, DexAwaitConsumer awaitConsumer) {
        return new Object();
    }
}
