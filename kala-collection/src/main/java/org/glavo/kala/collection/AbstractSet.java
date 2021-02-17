package org.glavo.kala.collection;

import org.glavo.kala.traversable.Iterators;

public abstract class AbstractSet<E> extends AbstractCollection<E> {
    @Override
    public int hashCode() {
        return Iterators.hash(iterator()) + SET_HASH_MAGIC;
    }
}
