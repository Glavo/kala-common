package kala.collection.mutable;

import kala.collection.SeqIterator;
import kala.collection.internal.SeqIterators;

public interface MutableSeqIterator<E> extends SeqIterator<E> {

    @SuppressWarnings("unchecked")
    static <E> MutableSeqIterator<E> empty() {
        return (MutableSeqIterator<E>) SeqIterators.EMPTY_MUTABLE;
    }

    @Override
    void set(E e);
}
