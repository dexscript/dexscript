package com.dexscript.ast;

public interface DexPackage {

    DexPackage DUMMY = new DexPackage() {
        @Override
        public String toString() {
            return "DUMMY";
        }
    };
}
