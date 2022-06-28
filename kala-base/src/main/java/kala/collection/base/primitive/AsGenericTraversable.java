package kala.collection.base.primitive;

import kala.collection.base.Traversable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

final class AsGenericTraversable<T> implements Traversable<T> {
    private final PrimitiveTraversable<T> source;

    AsGenericTraversable(PrimitiveTraversable<T> source) {
        this.source = source;
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public int knownSize() {
        return source.knownSize();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return source.iterator();
    }

    @Override
    public String toString() {
        return "AsGenericTraversable[" + source.toString() + "]";
    }
}
