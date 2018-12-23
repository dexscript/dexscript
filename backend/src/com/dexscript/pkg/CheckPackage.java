package com.dexscript.pkg;


import com.dexscript.analyze.CheckSemanticError;
import com.dexscript.analyze.CheckSyntaxError;
import com.dexscript.ast.DexFile;
import com.dexscript.ast.DexInterface;
import com.dexscript.ast.DexTopLevelDecl;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.elem.DexParam;
import com.dexscript.ast.inf.DexInfMethod;
import com.dexscript.shim.OutShim;
import com.dexscript.shim.actor.ActorType;
import com.dexscript.type.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.dexscript.pkg.FS.$p;

public class CheckPackage {

    static class Failed extends RuntimeException {
    }

    public static boolean $(String pathStr) throws IOException {
        try {
            return check(pathStr);
        } catch (Failed e) {
            return false;
        }
    }

    public static boolean check(String pathStr) throws IOException {
        Path pkgPath = $p(pathStr);
        if (!Files.isDirectory(pkgPath)) {
            System.out.println("package directory not found: " + pkgPath);
            return false;
        }
        List<DexFile> dexFiles = new ArrayList<>();
        Files.list(pkgPath).forEach(path -> {
            try {
                Text src = new Text(Files.readAllBytes(path));
                DexFile dexFile = new DexFile(src, path.getFileName().toString());
                dexFiles.add(dexFile);
            } catch (IOException e) {
                System.out.println("failed to read file: " + path);
                throw new Failed();
            }
        });
        if (hasSyntaxError(dexFiles)) {
            return false;
        }
        if (hasSemanticError(dexFiles)) {
            return false;
        }
        return true;
    }

    private static boolean hasSemanticError(List<DexFile> dexFiles) {
        boolean foundSpiFile = false;
        TypeSystem ts = new TypeSystem();
        OutShim oShim = new OutShim(ts);
        for (DexFile dexFile : dexFiles) {
            if (dexFile.fileName().equals("__spi__.ds")) {
                defineSpiFile(oShim, dexFile);
                foundSpiFile = true;
            } else {
                defineNormalFile(oShim, dexFile);
            }
        }
        if (!foundSpiFile) {
            System.out.println("missing __spi__.ds");
            return true;
        }
        for (DexFile dexFile : dexFiles) {
            if (new CheckSemanticError(ts, dexFile).hasError()) {
                return true;
            }
        }
        return false;
    }

    private static void defineNormalFile(OutShim oShim, DexFile dexFile) {
        TypeSystem ts = oShim.typeSystem();
        for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
            if (topLevelDecl.inf() != null) {
                if (isGlobalSpi(topLevelDecl)) {
                    System.out.println("can not define interface :: in file other than __spi__.ds");
                    throw new Failed();
                }
                ts.defineInterface(topLevelDecl.inf());
            } else if (topLevelDecl.actor() != null) {
                ts.defineType(new ActorType(oShim, topLevelDecl.actor()));
            }
        }
    }

    private static void defineSpiFile(OutShim oShim, DexFile dexFile) {
        TypeSystem ts = oShim.typeSystem();
        for (DexTopLevelDecl topLevelDecl : dexFile.topLevelDecls()) {
            if (topLevelDecl.actor() != null) {
                System.out.println("can not define function in __spi__.ds");
                throw new Failed();
            }
            if (topLevelDecl.inf() != null) {
                if (isGlobalSpi(topLevelDecl)) {
                    defineGlobalSPI(ts, topLevelDecl.inf());
                } else {
                    ts.defineInterface(topLevelDecl.inf());
                }
            }
        }
    }

    private static boolean isGlobalSpi(DexTopLevelDecl topLevelDecl) {
        return "::".equals(topLevelDecl.inf().identifier().toString());
    }

    private static void defineGlobalSPI(TypeSystem ts, DexInterface inf) {
        for (DexInfMethod infMethod : inf.methods()) {
            String name = infMethod.identifier().toString();
            List<FunctionParam> params = new ArrayList<>();
            for (DexParam param : infMethod.sig().params()) {
                String paramName = param.paramName().toString();
                DType paramType = ResolveType.$(ts, null, param.paramType());
                params.add(new FunctionParam(paramName, paramType));
            }
            DType ret = ResolveType.$(ts, null, infMethod.sig().ret());
            ts.defineFunction(new FunctionType(ts, name, params, ret));
        }
    }

    private static boolean hasSyntaxError(List<DexFile> dexFiles) {
        for (DexFile dexFile : dexFiles) {
            dexFile.topLevelDecls();
            if (new CheckSyntaxError(dexFile).hasError()) {
                return true;
            }
        }
        return false;
    }
}
