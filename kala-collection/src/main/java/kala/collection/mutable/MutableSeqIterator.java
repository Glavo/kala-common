package kala.collection.mutable;

import kala.collection.SeqIterator;

public interface MutableSeqIterator<E> extends SeqIterator<E> {

    @Override
    @SuppressWarnings("deprecation")
    void set(E e);
}
