package com.dexscript.ast.core;

import com.dexscript.ast.DexPackage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class DexElement {

    public interface Visitor {
        void visit(DexElement elem);
    }

    private List<Object> attachments;
    protected DexElement parent;
    protected Text src;
    private String toStringCache;

    public DexElement(Text src) {
        this.src = src;
    }

    public <T> T attachmentOfType(Class<T> clazz) {
        if (attachments == null) {
            return null;
        }
        for (Object attachment : attachments) {
            if (clazz.isAssignableFrom(attachment.getClass())) {
                return (T) attachment;
            }
        }
        return null;
    }

    public <T> T attach(@NotNull T attachment) {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        attachments.add(attachment);
        return attachment;
    }

    public final Text src() {
        return src;
    }

    public int begin() {
        return src.begin;
    }
    public abstract int end();
    public abstract boolean matched();

    public DexSyntaxError syntaxError() {
        return null;
    }

    public final DexElement parent() {
        return parent;
    }

    public abstract void walkDown(Visitor visitor);
    public DexElement prev() {
        return parent();
    }

    public DexPackage pkg() {
        DexPackage pkg = attachmentOfType(DexPackage.class);
        if (pkg != null) {
            return pkg;
        }
        if (parent() == null) {
            throw new DexSyntaxException("pkg not attached to dex element");
        }
        pkg = parent().pkg();
        if (pkg == null) {
            throw new DexSyntaxException("pkg not attached to dex element");
        }
        attach(pkg);
        return pkg;
    }

    @Override
    public String toString() {
        if (toStringCache != null) {
            return toStringCache;
        }
        if (!matched()) {
            return "<unmatched>" + src() + "</unmatched>";
        }
        if (syntaxError() == null) {
            toStringCache = new Text(src().bytes, begin(), end()).toString();
            return toStringCache;
        }
        int errorPos = syntaxError().errorPos;
        if (errorPos < begin()) {
            return "<error/>" + new Text(src().bytes, begin(), end()).toString();
        }
        String part1 = new Text(src().bytes, begin(), errorPos).toString();
        String part2 = new Text(src().bytes, errorPos, end()).toString();
        return part1 + "<error/>" + part2;
    }
}
