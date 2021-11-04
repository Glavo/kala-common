package kala.collection.mutable;

import kala.collection.AbstractSeqIterator;

public abstract class AbstractMutableSeqIterator<E> extends AbstractSeqIterator<E> implements MutableSeqIterator<E> {
    protected AbstractMutableSeqIterator(int index) {
        super(index);
    }
}
