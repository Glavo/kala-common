package kala.collection.mutable.primitive;

import kala.collection.mutable.MutableListIterator;

public interface MutablePrimitiveListIterator<T, T_CONSUMER>
        extends MutablePrimitiveSeqIterator<T, T_CONSUMER>, MutableListIterator<T> {
    @Override
    void remove();
}
