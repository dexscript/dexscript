package com.dexscript.pkg;

import com.dexscript.ast.DexPackage;
import com.dexscript.type.composite.FakeActorTypeImpl;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FakeImportPackageImpl extends FakeActorTypeImpl implements ImportPackage.Impl {

    private final Map<String, DexPackage> packageMap = new HashMap<>();

    @Override
    public DexPackage pkg(Path pkgPath) {
        String pathStr = pkgPath.toString().replace("/", ".");
        if (!packageMap.containsKey(pathStr)) {
            packageMap.put(pathStr, new DexPackage(pathStr));
        }
        return packageMap.get(pathStr);
    }
}