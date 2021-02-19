package org.glavo.kala.collection;

import org.glavo.kala.collection.base.Iterators;

public abstract class AbstractSeq<E> extends AbstractCollection<E> implements Seq<E> {
    @Override
    public int hashCode() {
        return Iterators.hash(iterator()) + SEQ_HASH_MAGIC;
    }
}
