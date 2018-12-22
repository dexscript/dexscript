package com.dexscript.pkg;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

class FS {

    static FileSystem fs = FileSystems.getDefault();

    static Path $p(String first, String... more) {
        return fs.getPath(first, more);
    }
}
