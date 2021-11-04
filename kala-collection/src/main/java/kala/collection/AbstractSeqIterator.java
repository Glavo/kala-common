package kala.collection;

import kala.collection.base.AbstractIterator;

public abstract class AbstractSeqIterator<E> extends AbstractIterator<E> implements SeqIterator<E> {
    protected int cursor;

    protected AbstractSeqIterator(int index) {
        this.cursor = index;
    }

    @Override
    public int nextIndex() {
        return cursor;
    }

    @Override
    public int previousIndex() {
        return cursor - 1;
    }

    @Override
    public boolean hasPrevious() {
        return cursor > 0;
    }
}
