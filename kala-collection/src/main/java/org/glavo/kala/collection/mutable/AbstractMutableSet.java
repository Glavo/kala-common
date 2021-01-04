package org.glavo.kala.collection.mutable;

import org.glavo.kala.iterator.Iterators;
import org.glavo.kala.collection.AbstractCollection;
import org.glavo.kala.collection.Collection;

public abstract class AbstractMutableSet<E> extends AbstractCollection<E> implements MutableSet<E> {

    @Override
    public int hashCode() {
        return Iterators.hash(iterator()) + Collection.SET_HASH_MAGIC;
    }
}
