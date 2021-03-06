package org.glavo.kala.collection;

public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    @Override
    public int hashCode() {
        return Set.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Set<?> && Set.equals(this, ((Set<?>) obj));
    }
}
