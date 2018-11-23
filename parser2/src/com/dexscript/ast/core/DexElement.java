package com.dexscript.ast.core;

import java.util.ArrayList;
import java.util.List;

public abstract class DexElement {

    public interface Visitor {
        void visit(DexElement elem);
    }

    public static class Collector implements Visitor {

        public final List<DexElement> collected = new ArrayList<>();

        @Override
        public void visit(DexElement elem) {
            if (elem == null) {
                throw new NullPointerException("expect not null element");
            }
            collected.add(elem);
        }
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
            if (attachment.getClass() == clazz) {
                return (T) attachment;
            }
        }
        return null;
    }

    public <T> T attach(T attachment) {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        attachments.add(attachment);
        return attachment;
    }

    public final Text src() {
        return src;
    }
    public abstract int begin();
    public abstract int end();
    public abstract boolean matched();
    public abstract DexError err();

    public final DexElement parent() {
        return parent;
    }

    public abstract void walkDown(Visitor visitor);
    public DexElement prev() {
        return parent();
    }

    @Override
    public String toString() {
        if (toStringCache != null) {
            return toStringCache;
        }
        if (!matched()) {
            return "<unmatched>" + src() + "</unmatched>";
        }
        if (err() == null) {
            toStringCache = new Text(src().bytes, begin(), end()).toString();
            return toStringCache;
        }
        int errorPos = err().errorPos;
        if (errorPos < begin()) {
            return "<error/>" + new Text(src().bytes, begin(), end()).toString();
        }
        String part1 = new Text(src().bytes, begin(), errorPos).toString();
        String part2 = new Text(src().bytes, errorPos, end()).toString();
        return part1 + "<error/>" + part2;
    }
}
