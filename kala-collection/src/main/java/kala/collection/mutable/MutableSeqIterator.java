package kala.collection.mutable;

import kala.collection.SeqIterator;

public interface MutableSeqIterator<E> extends SeqIterator<E> {

    @Override
    void set(E e);
}
