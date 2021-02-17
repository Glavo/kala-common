package org.glavo.kala.collection.mutable;

import org.glavo.kala.traversable.Iterators;
import org.glavo.kala.collection.Collection;

public abstract class AbstractMutableSeq<E> extends AbstractMutableCollection<E> implements MutableSeq<E> {
    @Override
    public int hashCode() {
        return Iterators.hash(iterator()) + Collection.SEQ_HASH_MAGIC;
    }
}
