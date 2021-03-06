package org.glavo.kala.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public final class SimpleIterable<E> implements Iterable<E> {
    private final Iterable<? extends E> source;

    public SimpleIterable(Iterable<? extends E> source) {
        this.source = source;
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        return (Iterator<E>) source.iterator();
    }
}
